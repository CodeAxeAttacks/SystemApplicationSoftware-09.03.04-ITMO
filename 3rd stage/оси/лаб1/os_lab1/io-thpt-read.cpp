#include <iostream>
#include <fcntl.h>
#include <unistd.h>
#include <vector>
#include <chrono>
#include <random>
#include <sys/wait.h>

#define BLOCK_SIZE 1638

void generateFile(const char* filepath, size_t sizeInBytes) {
    int fd = open(filepath, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
    if (fd < 0) {
        perror("Failed to create file");
        return;
    }

    std::vector<char> buffer(BLOCK_SIZE, 0);
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0, 255);

    for (size_t i = 0; i < sizeInBytes; i += BLOCK_SIZE) {
        for (size_t j = 0; j < BLOCK_SIZE; ++j) {
            buffer[j] = static_cast<char>(dis(gen));
        }
        ssize_t bytes_written = write(fd, buffer.data(), std::min<size_t>(BLOCK_SIZE, sizeInBytes - i));
        if (bytes_written < 0) {
            perror("Error writing to file");
            close(fd);
            return;
        }
    }

    close(fd);
    std::cout << "File generated: " << filepath << " (" << sizeInBytes << " bytes)\n";
}

void measureThroughput(const char* filepath, int iterations) {
    char buffer[BLOCK_SIZE];
    int fd = open(filepath, O_RDONLY);
    if (fd < 0) {
        perror("Failed to open file");
        return;
    }

    auto start_time = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < iterations; ++i) {
        ssize_t bytes_read = read(fd, buffer, BLOCK_SIZE);
        if (bytes_read < 0) {
            perror("Error reading file");
            close(fd);
            return;
        }
        if (bytes_read == 0) {
            lseek(fd, 0, SEEK_SET);
        }
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    close(fd);

    auto elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);
    std::cout << "Total time: " << elapsed_time.count() << " ms\n";
}

void childProcess(const char* filepath, size_t fileSizeInBytes) {
    generateFile(filepath, fileSizeInBytes);
    exit(0);
}

int main(int argc, char* argv[]) {
    if (argc != 4) {
        std::cerr << "Usage: " << argv[0] << " <file_path> <file_size_in_MB> <iterations>\n";
        return 1;
    }

    const char* filepath = argv[1];
    size_t fileSizeInBytes = std::stoull(argv[2]) * 1024 * 1024;
    int iterations = std::stoi(argv[3]);

    pid_t pid = fork();
    if (pid == -1) {
        perror("Failed to fork process");
        return 1;
    }

    if (pid == 0) {
        childProcess(filepath, fileSizeInBytes);
    } else {
        waitpid(pid, nullptr, 0);
        measureThroughput(filepath, iterations);
    }

    return 0;
}
