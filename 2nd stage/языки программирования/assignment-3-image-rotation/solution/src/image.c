#include "../include/image.h"
#include <stdlib.h>

void free_image(struct image *img) {
    free(img->data);
    img->data = NULL;
}
