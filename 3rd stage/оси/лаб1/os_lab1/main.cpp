#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <unistd.h>
#include <sys/wait.h>
#include <chrono>
#include <fcntl.h>
#include <cstring>

const char* HISTORY_FILE = "/Users/daniilbatmanov/CLionProjects/os_lab1/danell-history";

void addCommandToHistory(const std::string& command) {
    int fd = open(HISTORY_FILE, O_WRONLY | O_CREAT | O_APPEND, 0644);
    if (fd < 0) {
        perror("Failed to open history file");
        return;
    }

    std::string commandWithNewline = command + "\n";
    ssize_t bytes_written = write(fd, commandWithNewline.c_str(), commandWithNewline.size());
    if (bytes_written < 0) {
        perror("Failed to write to history file");
    }

    close(fd);
}

void showHistory() {
    int fd = open(HISTORY_FILE, O_RDONLY);
    if (fd < 0) {
        perror("Failed to open history file");
        return;
    }

    char buffer[1024];
    ssize_t bytes_read;

    int line_number = 1;
    while ((bytes_read = read(fd, buffer, sizeof(buffer) - 1)) > 0) {
        buffer[bytes_read] = '\0';
        char* line = strtok(buffer, "\n");
        while (line) {
            std::cout << line_number++ << ": " << line << "\n";
            line = strtok(nullptr, "\n");
        }
    }

    if (bytes_read < 0) {
        perror("Failed to read from history file");
    }

    close(fd);
}

void parseCommand(const std::string &input, std::string &command, std::vector<std::string> &args,
                  std::string &inputFile, std::string &outputFile, bool &appendOutput, bool &heredoc, std::string &heredocContent) {
    std::istringstream iss(input);
    iss >> command;

    std::string token;
    while (iss >> token) {
        if (token == ">") {
            iss >> outputFile;
            appendOutput = false;
        } else if (token == ">>") {
            iss >> outputFile;
            appendOutput = true;
        } else if (token == "<") {
            iss >> inputFile;
        } else if (token == "<<") {
            iss >> heredocContent;
            heredoc = true;
        } else {
            args.push_back(token);
        }
    }
}

std::vector<char*> convertArgs(const std::string &command, const std::vector<std::string> &args) {
    std::vector<char*> c_args;
    c_args.push_back(const_cast<char*>(command.c_str()));
    for (const auto &arg : args) {
        c_args.push_back(const_cast<char*>(arg.c_str()));
    }
    c_args.push_back(nullptr);
    return c_args;
}

void runCommand(const std::string &command, const std::vector<std::string> &args,
                const std::string &inputFile, const std::string &outputFile,
                bool appendOutput, bool heredoc, const std::string &heredocContent) {
    pid_t pid = fork();

    if (pid == -1) {
        perror("Fork failed");
        return;
    }

    auto start_time = std::chrono::high_resolution_clock::now();

    if (pid == 0) {
        if (!inputFile.empty()) {
            int fd = open(inputFile.c_str(), O_RDONLY);
            if (fd < 0) {
                perror("Failed to open input file");
                exit(1);
            }
            dup2(fd, STDIN_FILENO);
            close(fd);
        }

        if (!outputFile.empty()) {
            int fd = open(outputFile.c_str(), O_WRONLY | O_CREAT | (appendOutput ? O_APPEND : O_TRUNC), 0644);
            if (fd < 0) {
                perror("Failed to open output file");
                exit(1);
            }
            dup2(fd, STDOUT_FILENO);
            close(fd);
        }

        if (heredoc) {
            int pipefd[2];
            if (pipe(pipefd) == -1) {
                perror("Pipe failed");
                exit(1);
            }

            write(pipefd[1], heredocContent.c_str(), heredocContent.size());
            close(pipefd[1]);

            dup2(pipefd[0], STDIN_FILENO);
            close(pipefd[0]);
        }

        std::vector<char*> c_args = convertArgs(command, args);
        execvp(command.c_str(), c_args.data());
        perror("Execution failed");
        exit(1);
    } else { // Родительский процесс
        int status;
        waitpid(pid, &status, 0);

        auto end_time = std::chrono::high_resolution_clock::now();
        auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);

        std::cout << "Execution time: " << elapsed.count() << " ms\n";

        if (WIFEXITED(status)) {
            std::cout << "Program exited with code: " << WEXITSTATUS(status) << "\n";
        } else {
            std::cout << "Program terminated abnormally.\n";
        }
    }
}

void changeDirectory(const std::vector<std::string> &args) {
    if (args.empty()) {
        std::cerr << "cd: missing argument\n";
        return;
    }

    auto start_time = std::chrono::high_resolution_clock::now();
    if (chdir(args[0].c_str()) != 0) {
        perror("cd failed");
    }
    auto end_time = std::chrono::high_resolution_clock::now();

    auto elapsed = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time);
    std::cout << "Execution time (cd): " << elapsed.count() << " ms\n";
}

int main() {
    while (true) {
        std::cout << "danell> ";
        std::string input;
        std::getline(std::cin, input);

        if (input == "exit") {
            break;
        }

        std::string command, inputFile, outputFile, heredocContent;
        bool appendOutput = false, heredoc = false;
        std::vector<std::string> args;

        parseCommand(input, command, args, inputFile, outputFile, appendOutput, heredoc, heredocContent);

        if (command.empty()) {
            continue;
        }

        addCommandToHistory(input);

        if (command == "cd") {
            changeDirectory(args);
        } else if (command == "history") {
            showHistory();
        } else {
            runCommand(command, args, inputFile, outputFile, appendOutput, heredoc, heredocContent);
        }
    }

    return 0;
}
