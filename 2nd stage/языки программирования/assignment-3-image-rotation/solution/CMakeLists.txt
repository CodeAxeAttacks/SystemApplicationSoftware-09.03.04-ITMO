file(GLOB_RECURSE sources CONFIGURE_DEPENDS
    src/*.c
    src/*.h
    include/*.h
)

add_executable(image-transformer ${sources}
        include/utils.h
        src/utils.c)
target_include_directories(image-transformer PRIVATE src include)
