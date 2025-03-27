#include "../include/bmp.h"
#include "../include/fileio.h"
#include "../include/rotate.h"
#include "../include/utils.h"
#include <stdio.h>
#include <stdlib.h>

#define WRITE_ERR_INVALID_USAGE 1
#define WRITE_ERR_INVALID_ANGLE 2
#define WRITE_ERR_OPENING_SOURCE 3
#define WRITE_ERR_READING 4
#define WRITE_ERR_OPENING_TRANS 5
#define WRITE_ERR_WRITING_TRANS 6

int main(int argc, char* argv[]) {
    if (argc != 4) {
        fprintf(stderr, "Invalid usage, use: %s <source-image> <transformed-image> <angle>\n", argv[0]);
        return WRITE_ERR_INVALID_USAGE;
    }

    int valid_angles[] = {-270, -180, -90, 0, 90, 180, 270};
    int inputed_angle = atoi(argv[3]);

    if (!check_angle(valid_angles, sizeof(valid_angles) / sizeof(valid_angles[0]), inputed_angle)) {
        fprintf(stderr, "Invalid angle, use: {0, 90, 180, 270, -90, -180, -270}\n");
        return WRITE_ERR_INVALID_ANGLE;
    }

    FILE* source_file = open_file(argv[1], "rb");
    if (source_file == NULL) {
        fprintf(stderr, "Error with opening source file\n");
        return WRITE_ERR_OPENING_SOURCE;
    }

    struct image source_image;
    enum read_status status = from_bmp(source_file, &source_image);
    close_file(source_file);

    if (status != READ_OK) {
        fprintf(stderr, "Error with reading source image: %d\n", status);
        return WRITE_ERR_READING;
    }

    struct image transformed_image = rotate(&source_image, inputed_angle);

    FILE* transformed_file = open_file(argv[2], "wb");
    if (transformed_file == NULL) {
        fprintf(stderr, "Error with opening transformed file\n");
        free_image(&source_image);
        free_image(&transformed_image);
        return WRITE_ERR_OPENING_TRANS;
    }

    enum write_status write_status = to_bmp(transformed_file, &transformed_image);
    close_file(transformed_file);
    free_image(&source_image);
    free_image(&transformed_image);

    if (write_status != WRITE_OK) {
        fprintf(stderr, "Error with writing transformed image: %d\n", write_status);
        return WRITE_ERR_WRITING_TRANS;
    }

    return 0;
}
