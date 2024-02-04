#include "mem.h"
#include <assert.h>
#include <stdio.h>

void check_memory_allocation_success() {
    heap_init(0);
    printf("--> TEST #1\n Check memory allocation. Result:");
    void* mem1 = _malloc(100);
    assert(mem1 != NULL);
    _free(mem1);
    heap_term();
    printf(" Success!\n");
}

void check_memory_freeing_single_block() {
    heap_init(0);
    printf("--> TEST #2\n Check memory freeing single block. Result:");
    void* mem1 = _malloc(100);
    void* mem2 = _malloc(200);
    _free(mem1);
    _free(mem2);
    heap_term();
    printf(" Success!\n");
}

void check_memory_freeing_double_block() {
    heap_init(0);
    printf("--> TEST #3\n Check memory freeing double block. Result:");
    void* mem1 = _malloc(100);
    void* mem2 = _malloc(200);
    void* mem3 = _malloc(300);
    _free(mem1);
    _free(mem2);
    _free(mem3);
    heap_term();
    printf(" Success!\n");
}

void check_expanded_memory_capacity() {
    heap_init(0);
    printf("--> TEST #4\n Check expanded memory capacity. Result:");
    void* mem1 = _malloc(100000);
    assert(mem1 != NULL);
    _free(mem1);
    heap_term();
    printf(" Success!\n");
}

void check_memory_grow_within_address_limit() {
    heap_init(0);
    printf("--> TEST #5\n Check memory grow within address limit. Result:");
    void* mem1 = _malloc(100000);
    void* mem2 = _malloc(100000);
    assert(mem1 != NULL && mem2 != NULL);
    _free(mem1);
    void* mem3 = _malloc(200000);
    assert(mem3 != NULL);
    _free(mem2);
    _free(mem3);
    heap_term();
    printf(" Success!\n");
}

int main() {
    check_memory_allocation_success();
    check_memory_freeing_single_block();
    check_memory_freeing_double_block();
    check_expanded_memory_capacity();
    check_memory_grow_within_address_limit();

    printf("\n!!! SUCCESS, ALL OF THE TESTS (5/5) ARE PASSED !!!\n");
    return 0;
}