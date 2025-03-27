#ifndef LAB2_CACHE_H
#define LAB2_CACHE_H

#include <cstddef>
#include <unordered_map>
#include <vector>

// Структура страницы в кэше
struct CachePage {
    int fd;                   // Дескриптор файла
    off_t offset;             // Смещение
    std::vector<char> data;   // Данные страницы
};

struct PairHash {
    template <typename T1, typename T2>
    std::size_t operator()(const std::pair<T1, T2>& p) const { // Дескриптор файла + смещение
        return std::hash<T1>()(p.first) ^ (std::hash<T2>()(p.second) << 1);
    }
};

class Cache {
public:
    Cache(size_t capacity);
    ~Cache();

    ssize_t read(int fd, void* buf, size_t count, off_t offset);
    ssize_t write(int fd, const void* buf, size_t count, off_t offset);
    int open(const char* path);
    int close(int fd);
    off_t lseek(int fd, off_t offset, int whence);
    int fsync(int fd);

private:
    size_t capacity_;
    std::unordered_map<std::pair<int, off_t>, CachePage, PairHash> cache_; // Кэш страниц
    std::unordered_map<int, off_t> file_positions_; // Позиция указателя для каждого файла

    void evict(); // Вытеснение страницы по LIFO
};

#endif // LAB2_CACHE_H
