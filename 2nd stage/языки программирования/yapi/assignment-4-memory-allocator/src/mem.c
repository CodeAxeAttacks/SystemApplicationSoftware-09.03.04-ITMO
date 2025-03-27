#define _DEFAULT_SOURCE

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "mem_internals.h"
#include "mem.h"
#include "util.h"

void debug_block(struct block_header* b, const char* fmt, ...);
void debug(const char* fmt, ...);

extern inline block_size size_from_capacity(block_capacity cap);
extern inline block_capacity capacity_from_size(block_size sz);

static bool block_is_big_enough(size_t query, struct block_header* block) {
    return block->capacity.bytes >= query;
}

static size_t pages_count(size_t mem) {
    return mem / getpagesize() + ((mem % getpagesize()) > 0);
}

static size_t round_pages(size_t mem) {
    return getpagesize() * pages_count(mem);
}

static void block_init(void* restrict addr, block_size block_sz, void* restrict next) {
    *((struct block_header*)addr) = (struct block_header) {
            .next = next,
            .capacity = capacity_from_size(block_sz),
            .is_free = true
    };
}

static size_t region_actual_size(size_t query) {
    return size_max(round_pages(query), REGION_MIN_SIZE);
}

extern inline bool region_is_invalid(const struct region* r);

static void* map_pages(const void* addr, size_t length, int additional_flags) {
    return mmap((void*)addr, length, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | additional_flags, -1, 0);
}

static struct region alloc_region(void const* addr, size_t query) {
    block_size b_size = (block_size){region_actual_size(size_from_capacity((block_capacity){query}).bytes)};
    void* malloc_addr = map_pages(addr, b_size.bytes, 0x100000);

    if (malloc_addr == MAP_FAILED) {
        malloc_addr = map_pages(addr, b_size.bytes, 0);
        if (malloc_addr == MAP_FAILED) {
            return REGION_INVALID;
        }
    }

    block_init(malloc_addr, (block_size){.bytes = b_size.bytes}, NULL);
    struct region region = {.addr = malloc_addr, .size = b_size.bytes, .extends = malloc_addr == addr};
    return region;
}

static void* block_after(const struct block_header* block);

void* heap_init(size_t initial) {
    const struct region region = alloc_region(HEAP_START, initial);
    if (region_is_invalid(&region)) {
        return NULL;
    }
    return region.addr;
}

static bool blocks_continuous(
        const struct block_header* fst,
        const struct block_header* snd
);

void heap_term() {
    struct block_header* block = (struct block_header*)HEAP_START;
    void* region_start = HEAP_START;
    size_t region_size = 0;

    while (block != NULL) {
        region_size += size_from_capacity(block->capacity).bytes;
        struct block_header* next_block = block->next;

        if (next_block == NULL || !blocks_continuous(block, next_block)) {
            munmap(region_start, region_size);
            region_start = next_block;
            region_size = 0;
        }

        block = next_block;
    }
}

#define BLOCK_MIN_CAPACITY 24

static bool block_splittable(struct block_header* restrict block, size_t query) {
    return block->is_free && query + offsetof(struct block_header, contents) + BLOCK_MIN_CAPACITY <= block->capacity.bytes;
}

static bool split_if_too_big(struct block_header* block, size_t query) {
    if (block_splittable(block, query)) {
        block_capacity orig_capacity = block->capacity;
        block->capacity.bytes = query;
        void* new_block_address = block_after(block);
        block_init(new_block_address, (block_size){.bytes = orig_capacity.bytes - query}, block->next);
        block->next = new_block_address;
        return true;
    }
    return false;
}

static void* block_after(const struct block_header* block) {
    return (void*)(block->contents + block->capacity.bytes);
}

static bool blocks_continuous(
        const struct block_header* fst,
        const struct block_header* snd
) {
    return (void*)snd == block_after(fst);
}

static bool mergeable(
        const struct block_header* restrict fst,
        const struct block_header* restrict snd
) {
    return fst->is_free && snd->is_free && blocks_continuous(fst, snd);
}

static bool try_merge_with_next(struct block_header* block) {
    struct block_header* next_block = block->next;
    if (block == NULL || next_block == NULL) {
        return false;
    }

    if (mergeable(block, next_block)) {
        block_init(block, (block_size){.bytes = size_from_capacity(block->capacity).bytes +
                                                size_from_capacity(next_block->capacity).bytes}, next_block->next);
        return true;
    }
    return false;
}

struct block_search_result {
    enum {BSR_FOUND_GOOD_BLOCK, BSR_REACHED_END_NOT_FOUND, BSR_CORRUPTED} type;
    struct block_header* block;
};

static struct block_search_result find_good_or_last(struct block_header* restrict block, size_t sz) {
    struct block_header* last_block = NULL;

    while (block) {
        while (try_merge_with_next(block));

        if (block->is_free && block_is_big_enough(sz, block)) {
            return (struct block_search_result){BSR_FOUND_GOOD_BLOCK, block};
        }

        last_block = block;
        block = block->next;
    }

    return (struct block_search_result){BSR_REACHED_END_NOT_FOUND, last_block};
}

static struct block_search_result try_memalloc_existing(size_t query, struct block_header* block) {
    size_t adjusted_query = (query < BLOCK_MIN_CAPACITY) ? BLOCK_MIN_CAPACITY : query;
    struct block_search_result result = find_good_or_last(block, adjusted_query);

    if (result.type == BSR_FOUND_GOOD_BLOCK) {
        split_if_too_big(result.block, adjusted_query);
        result.block->is_free = false;
    }

    return result;
}

static struct block_header* grow_heap(struct block_header* restrict last, size_t query) {
    if (!last) {
        return NULL;
    }

    struct region new_region = alloc_region(block_after(last), size_max(query, BLOCK_MIN_CAPACITY));

    if (region_is_invalid(&new_region)) {
        return NULL;
    }

    block_init(new_region.addr, (block_size){.bytes = new_region.size}, NULL);
    last->next = new_region.addr;

    if (last->is_free && try_merge_with_next(last)) {
        return last;
    }

    return last->next;
}

static struct block_header* memalloc(size_t query, struct block_header* heap_start) {
    struct block_search_result result = try_memalloc_existing(size_max(query, BLOCK_MIN_CAPACITY), heap_start);

    if (result.type == BSR_FOUND_GOOD_BLOCK) {
        return result.block;
    }

    if (result.type == BSR_REACHED_END_NOT_FOUND) {
        result = try_memalloc_existing(size_max(query, BLOCK_MIN_CAPACITY), grow_heap(result.block, size_max(query, BLOCK_MIN_CAPACITY)));

        if (result.type == BSR_FOUND_GOOD_BLOCK) {
            return result.block;
        }
    }

    return NULL;
}

void* _malloc(size_t query) {
    struct block_header* const addr = memalloc(query, (struct block_header*)HEAP_START);

    if (addr) {
        return addr->contents;
    } else {
        return NULL;
    }
}

static struct block_header* block_get_header(void* contents) {
    return (struct block_header*)(((uint8_t*)contents) - offsetof(struct block_header, contents));
}

void _free(void* mem) {
    if (!mem) {
        return;
    }

    struct block_header* header = block_get_header(mem);
    header->is_free = true;

    while (try_merge_with_next(header));
}
