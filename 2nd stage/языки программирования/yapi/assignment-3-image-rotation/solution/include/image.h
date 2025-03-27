#ifndef IMAGE_H
#define IMAGE_H

#include <stdint.h>

struct pixel {
    uint8_t b, g, r;
};

struct image {
    uint64_t width, height;
    struct pixel* data;
};

void free_image(struct image* img);

#endif
