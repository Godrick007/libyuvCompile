# CMakeLists for libyuv
# Originally created for "roxlu build system" to compile libyuv on windows
# Run with -DTEST=ON to build unit tests

CMAKE_MINIMUM_REQUIRED(VERSION 2.8)
OPTION(TEST "Built unit tests" OFF)

SET(ly_base_dir src/main/cpp)
SET(ly_inc_dir ${ly_base_dir}/include)


INCLUDE_DIRECTORIES(BEFORE ${ly_inc_dir})


add_library(yuv-rgb SHARED
        src/main/cpp/native-lib.cpp)


add_library(yuv SHARED IMPORTED)
set_target_properties(yuv
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libyuv.so
        )

target_link_libraries(
        yuv-rgb
        yuv
)
