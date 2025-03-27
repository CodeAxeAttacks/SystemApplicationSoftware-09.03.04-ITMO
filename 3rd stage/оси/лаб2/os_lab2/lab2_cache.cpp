#include "lab2_cache.h"
#include <fcntl.h>
#include <unistd.h>
#include <cstring>
#include <vector>
#include <unordered_map>

Cache::Cache(size_t capacity) : capacity_(capacity) {}

Cache::~Cache() {
    for (const auto& [key, page] : cache_) {
        fsync(page.fd);
    }
}

int Cache::open(const char* path) {
    int fd = ::open(path, O_RDWR);
    if (fd < 0) {
        perror("Error opening file");
        return -1;
    }
    file_positions_[fd] = 0; // Инициализация позиции
    return fd;
}

int Cache::close(int fd) {
    fsync(fd);
    file_positions_.erase(fd);
    // Удаляем все страницы, связанные с файлом
    for (auto it = cache_.begin(); it != cache_.end();) {
        if (it->second.fd == fd) {
            it = cache_.erase(it);
        } else {
            ++it;
        }
    }
    return ::close(fd);
}

off_t Cache::lseek(int fd, off_t offset, int whence) {
    return file_positions_[fd] = ::lseek(fd, offset, whence);
}

ssize_t Cache::read(int fd, void* buf, size_t count, off_t offset) {
    auto key = std::make_pair(fd, offset);

    auto it = cache_.find(key);
    if (it != cache_.end()) {
        // Если страница найдена в кэше
        std::memcpy(buf, it->second.data.data(), count);
        return count;
    }

    // Если страницы нет в кэше, читаем с диска
    std::vector<char> data(count);
    ssize_t bytesRead = ::pread(fd, data.data(), count, offset);
    if (bytesRead < 0) {
        perror("Error reading file");
        return -1;
    }

    // Добавляем страницу в кэш
    if (cache_.size() >= capacity_) evict();
    cache_[key] = {fd, offset, std::move(data)};
    std::memcpy(buf, cache_[key].data.data(), count);
    return bytesRead;
}

ssize_t Cache::write(int fd, const void* buf, size_t count, off_t offset) {
    auto key = std::make_pair(fd, offset);

    // Добавляем/обновляем страницу в кэше
    auto it = cache_.find(key);
    if (it == cache_.end()) {
        if (cache_.size() >= capacity_) evict();
        cache_[key] = {fd, offset, std::vector<char>((char*)buf, (char*)buf + count)};
    } else {
        std::copy((char*)buf, (char*)buf + count, it->second.data.begin());
    }

    // Пишем данные на диск
    return ::pwrite(fd, buf, count, offset);
}

int Cache::fsync(int fd) {
    for (auto it = cache_.begin(); it != cache_.end();) {
        if (it->second.fd == fd) {
            it = cache_.erase(it);
        } else {
            ++it;
        }
    }
    return ::fsync(fd);
}

void Cache::evict() {
    if (!cache_.empty()) {
        auto last = cache_.begin();
        cache_.erase(last);
    }
}
