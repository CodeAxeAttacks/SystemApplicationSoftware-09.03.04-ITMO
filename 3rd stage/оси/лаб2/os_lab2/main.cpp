#include "lab2_cache.h"

int main() {
    Cache cache(100); // 10 страниц

    int fd = cache.open("testfile.txt");
    char buf[128];
    cache.read(fd, buf, sizeof(buf), 0);
    cache.write(fd, "Hello, world!", 13, 0);
    cache.fsync(fd);
    cache.close(fd);

    return 0;
}
