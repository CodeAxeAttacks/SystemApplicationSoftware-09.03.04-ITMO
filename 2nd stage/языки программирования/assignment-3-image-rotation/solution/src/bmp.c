#include "../include/bmp.h"
#include <stdlib.h>

#define BYTES_PER_PIXEL 3
#define BITS_PER_PIXEL 24
#define BMP_SIGNATURE 0x4D42
#define INFO_PIXEL_SIZE 40

static uint32_t get_padding(uint32_t width) {
    return (-1 * (width * sizeof(struct pixel))) % 4;
}

enum read_status from_bmp(FILE* in, struct image* img) {
    struct bmp_header header;
    fread(&header, sizeof(struct bmp_header), 1, in);

    if (header.bfType != BMP_SIGNATURE) {
        return READ_INVALID_SIGNATURE;
    }

    if (header.biBitCount != BITS_PER_PIXEL) {
        return READ_INVALID_BITS;
    }

    img->width = header.biWidth;
    img->height = header.biHeight;

    img->data = (struct pixel*)malloc(sizeof(struct pixel) * img->width * img->height);

    fseek(in, header.bOffBits, 0);
    for (uint64_t y = 0; y < img->height; ++y) {
        for (uint64_t x = 0; x < img->width; ++x) {
            struct pixel* pixel = &img->data[y * img->width + x];
            fread(pixel, sizeof(struct pixel), 1, in);
        }

        fseek(in, (long)(get_padding(img->width)), 1);
    }

    return READ_OK;
}

enum write_status to_bmp(FILE* out, struct image const* img) {
    struct bmp_header header = {
            .bfType = BMP_SIGNATURE,
            .bfileSize = sizeof(struct bmp_header) + img->width * img->height * BYTES_PER_PIXEL,
            .bfReserved = 0,
            .bOffBits = sizeof(struct bmp_header),
            .biSize = INFO_PIXEL_SIZE,
            .biWidth = img->width,
            .biHeight = img->height,
            .biPlanes = 1,
            .biBitCount = BITS_PER_PIXEL,
            .biCompression = 0,
            .biSizeImage = 0,
            .biXPelsPerMeter = 0,
            .biYPelsPerMeter = 0,
            .biClrUsed = 0,
            .biClrImportant = 0
    };

    fwrite(&header, sizeof(struct bmp_header), 1, out);

    for (uint64_t y = 0; y < img->height; ++y) {
        for (uint64_t x = 0; x < img->width; ++x) {
            struct pixel const* pixel = &img->data[y * img->width + x];
            fwrite(pixel, sizeof(struct pixel), 1, out);
        }

        uint8_t padding[3] = {0, 0, 0};
        fwrite(padding, sizeof(uint8_t), ((get_padding(img->width))), out);
    }

    return WRITE_OK;
}
