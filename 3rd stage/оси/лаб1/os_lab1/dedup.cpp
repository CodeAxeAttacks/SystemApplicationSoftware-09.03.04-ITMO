#include <iostream>
#include <vector>
#include <unordered_set>
#include <cstdlib>
#include <chrono>

std::vector<int> generateArray(int size) {
    std::vector<int> array(size);
    for (int i = 0; i < size; ++i) {
        array[i] = rand() % 100;
    }
    return array;
}

void deduplicateArray(const std::vector<int>& input, std::vector<int>& output) {
    std::unordered_set<int> seen;
    for (int num : input) {
        if (seen.insert(num).second) {
            output.push_back(num);
        }
    }
}

void measureDeduplication(int arraySize, int iterations) {
    auto input = generateArray(arraySize);

    auto start_time = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < iterations; ++i) {
        std::vector<int> output;
        deduplicateArray(input, output);
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);

    std::cout << "Total time for deduplication: " << elapsed_time.count() << " ms\n";
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        std::cout << "Usage: " << argv[0] << " <array_size> <iterations>\n";
        return 1;
    }

    int arraySize = std::stoi(argv[1]);
    int iterations = std::stoi(argv[2]);

    measureDeduplication(arraySize, iterations);
    return 0;
}
