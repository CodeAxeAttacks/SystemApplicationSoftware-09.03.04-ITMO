#include "../include/rotate.h"
#include <stdlib.h>

struct image rotate(struct image const* source, int angle) {
    struct image result;

    if ((angle + 360) % 180 != 0) {
        result.width = source->height;
        result.height = source->width;
    } else {
        result.width = source->width;
        result.height = source->height;
    }

    result.data = malloc(sizeof(struct pixel) * result.width * result.height);
    if (!result.data) {
        result.width = 0;
        result.height = 0;
        return result;
    }

    for (uint64_t y = 0; y < source->height; ++y) {
        for (uint64_t x = 0; x < source->width; ++x) {
            uint64_t dst_x, dst_y;

            switch (angle) {
                case 90:
                case -270:
                    dst_x = y;
                    dst_y = source->width - x - 1;
                    break;
                case 180:
                case -180:
                    dst_x = source->width - x - 1;
                    dst_y = source->height - y - 1;
                    break;
                case 270:
                case -90:
                    dst_x = source->height - y - 1;
                    dst_y = x;
                    break;
                default:
                    dst_x = x;
                    dst_y = y;
            }

            uint64_t dst_index = dst_y * result.width + dst_x;
            uint64_t src_index = y * source->width + x;

            result.data[dst_index] = source->data[src_index];
        }
    }

    return result;
}
