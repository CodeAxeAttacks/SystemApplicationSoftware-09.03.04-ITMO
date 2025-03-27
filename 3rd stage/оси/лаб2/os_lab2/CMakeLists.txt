cmake_minimum_required(VERSION 3.10)
project(os_lab2)

set(CMAKE_CXX_STANDARD 17)

# Укажите исходники и библиотеку
add_library(lab2_cache SHARED lab2_cache.cpp)

# Основной исполняемый файл
add_executable(main main.cpp)

# Линкуем библиотеку к main
target_link_libraries(main lab2_cache)

# Добавляем еще один исполняемый файл для нагрузки
add_executable(dedup dedup.cpp)
target_link_libraries(dedup lab2_cache)
