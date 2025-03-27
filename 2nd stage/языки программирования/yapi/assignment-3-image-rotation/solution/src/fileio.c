#include "../include/fileio.h"

FILE* open_file(char const* filename, char const* mode) {
    FILE* file = fopen(filename, mode);
    return file;
}

void close_file(FILE* file) {
    if (file != NULL) {
        fclose(file);
    }
}
