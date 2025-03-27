#include "../include/utils.h"

int check_angle(int* array_of_valid_angles, size_t array_size, int inputed_angle) {
    for (size_t i = 0; i < array_size; ++i) {
        if (array_of_valid_angles[i] == inputed_angle) {
            return 1;
        }
    }
    return 0;
}
