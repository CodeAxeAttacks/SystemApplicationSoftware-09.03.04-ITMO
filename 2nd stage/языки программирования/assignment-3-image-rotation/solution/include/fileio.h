#ifndef FILEIO_H
#define FILEIO_H

#include "image.h"
#include <stdio.h>

FILE* open_file(char const* filename, char const* mode);

void close_file(FILE* file);

#endif
