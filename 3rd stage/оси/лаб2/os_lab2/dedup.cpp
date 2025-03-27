#include "lab2_cache.h"
#include <vector>
#include <unordered_set>
#include <cstdlib>
#include <chrono>
#include <fcntl.h>
#include <unistd.h>
#include <cstring>
#include <cstdio>

// Генерация массива случайных чисел
std::vector<int> generateArray(int size) {
    std::vector<int> array(size);
    for (int i = 0; i < size; ++i) {
        array[i] = rand() % 100;
    }
    return array;
}

// Удаление дубликатов из массива через кэш
void deduplicateArray(Cache& cache, int fd, int arraySize, std::vector<int>& output) {
    std::unordered_set<int> seen;
    int buffer;
    for (int i = 0; i < arraySize; ++i) {
        // Читаем элемент через кэш
        cache.read(fd, &buffer, sizeof(buffer), i * sizeof(int));
        if (seen.insert(buffer).second) {
            output.push_back(buffer);
        }
    }
}

// Измерение времени выполнения дедупликации
void measureDeduplication(Cache& cache, int fd, int arraySize, int iterations) {
    auto start_time = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < iterations; ++i) {
        std::vector<int> output;
        deduplicateArray(cache, fd, arraySize, output);
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);

    char buffer[128];
    snprintf(buffer, sizeof(buffer), "Total time for deduplication: %lld ms\n", elapsed_time.count());
    write(STDOUT_FILENO, buffer, strlen(buffer));
}

// Запись массива в файл через кэш
void writeArrayToFile(Cache& cache, int fd, const std::vector<int>& array) {
    for (size_t i = 0; i < array.size(); ++i) {
        cache.write(fd, &array[i], sizeof(array[i]), i * sizeof(int));
    }
}

// Основная функция
int main(int argc, char* argv[]) {
    if (argc != 3) {
        const char* usage_message = "Usage: <program> <array_size> <iterations>\n";
        write(STDOUT_FILENO, usage_message, strlen(usage_message));
        return 1;
    }

    // Преобразование аргументов
    int arraySize = atoi(argv[1]);
    int iterations = atoi(argv[2]);

    // Создаем кэш
    Cache cache(10); // 10 страниц в кэше

    // Открываем файл для работы с кэшем
    int fd = cache.open("datafile.bin");
    if (fd < 0) {
        const char* error_message = "Failed to open file.\n";
        write(STDERR_FILENO, error_message, strlen(error_message));
        return 1;
    }

    // Генерируем массив и записываем его в файл через кэш
    auto input = generateArray(arraySize);
    writeArrayToFile(cache, fd, input);

    // Измеряем производительность дедупликации
    measureDeduplication(cache, fd, arraySize, iterations);

    // Закрываем файл
    cache.close(fd);

    return 0;
}
