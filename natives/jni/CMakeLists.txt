cmake_minimum_required(VERSION 3.11)
project(jcusegsort)

set(PROJECT_VERSION 10.2.0)

if(CMAKE_HOST_WIN32)
    set(PROJECT_OS "windows")
elseif(CMAKE_HOST_APPLE)
    set(PROJECT_OS "apple")
    set(CMAKE_SKIP_RPATH FALSE)
elseif(CMAKE_HOST_UNIX)
    set(PROJECT_OS "linux")
endif()

if(CMAKE_SYSTEM_PROCESSOR MATCHES "ppc*")
    if(CMAKE_SIZEOF_VOID_P EQUAL 8)
        set(PROJECT_ARCH "ppc_64")
    endif()
else()
    if(CMAKE_SIZEOF_VOID_P EQUAL 8)
        set(PROJECT_ARCH "x86_64")
    else()
        set(PROJECT_ARCH "x86")
    endif()
endif()

set(NATIVE_LIBRARY_DIRECTORY
        ../nativeLibraries/${PROJECT_OS}/${PROJECT_ARCH}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY
        ${PROJECT_SOURCE_DIR}/${NATIVE_LIBRARY_DIRECTORY})
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY
        ${PROJECT_SOURCE_DIR}/${NATIVE_LIBRARY_DIRECTORY})
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
foreach(CONFIGURATION_TYPE ${CMAKE_CONFIGURATION_TYPES})
    string(TOUPPER ${CONFIGURATION_TYPE} CONFIGURATION_TYPE)
    set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_${CONFIGURATION_TYPE}
            ${PROJECT_SOURCE_DIR}/${NATIVE_LIBRARY_DIRECTORY})
    set(CMAKE_LIBRARY_OUTPUT_DIRECTORY_${CONFIGURATION_TYPE}
            ${PROJECT_SOURCE_DIR}/${NATIVE_LIBRARY_DIRECTORY})
    set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY_${CONFIGURATION_TYPE}
            ${CMAKE_BINARY_DIR}/lib)
endforeach(CONFIGURATION_TYPE CMAKE_CONFIGURATION_TYPES)

if (NOT CMAKE_CUDA_SHARED_LINKER_FLAGS_INITIALIZED)
    if(CMAKE_HOST_APPLE)
        string(REGEX REPLACE "^([0-9]+)\\.([0-9]+).*$" "\\1"
                DARWIN_MAJOR_VERSION ${CMAKE_SYSTEM_VERSION})
        if(${DARWIN_MAJOR_VERSION} GREATER 14)
            set(CMAKE_SHARED_LINKER_FLAGS
                    "${CMAKE_SHARED_LINKER_FLAGS} -Xlinker -F/Library/Frameworks -Xlinker -framework -Xlinker CUDA")
        endif()
    endif()
    set(CMAKE_CUDA_SHARED_LINKER_FLAGS_INITIALIZED "true")
endif()

FIND_PACKAGE(CUDA REQUIRED)
FIND_PACKAGE(JNI REQUIRED)

set(CUDA_NVCC_FLAGS -arch=sm_35 --expt-relaxed-constexpr)
set(CMAKE_CXX_STANDARD 11)
if(CMAKE_HOST_UNIX)
    set(CMAKE_CXX_FLAGS "-fPIC " )
endif()

INCLUDE(FindCUDA)
include_directories(${JNI_INCLUDE_DIRS})

include_directories(bb_segsort)
include_directories(bb_segsort/bb)
include_directories(bb_segsort/bb/k)
include_directories(bb_segsort/bb/kv)
include_directories(bb_segsort/bb/matrix)
include_directories(bb_segsort/bb/matrix/k)
include_directories(bb_segsort/bb/matrix/kv)
include_directories(bb_segsort/bb/segments)
include_directories(bb_segsort/bb/segments/k)
include_directories(bb_segsort/bb/segments/kv)

cuda_add_library(${PROJECT_NAME}-cuda
        bb_segsort/bb/k/bb_context.h
        bb_segsort/bb/k/bb_context.hpp
        bb_segsort/bb/k/bb_exch.hpp
        bb_segsort/bb/kv/bb_context.h
        bb_segsort/bb/kv/bb_context.hpp
        bb_segsort/bb/kv/bb_exch.hpp
        bb_segsort/bb/matrix/k/bb_comput_l.hpp
        bb_segsort/bb/matrix/k/bb_comput_s.hpp
        bb_segsort/bb/matrix/k/bb_segsort.h
        bb_segsort/bb/matrix/k/bb_segsort.hpp
        bb_segsort/bb/matrix/kv/bb_comput_l.hpp
        bb_segsort/bb/matrix/kv/bb_comput_s.hpp
        bb_segsort/bb/matrix/kv/bb_segsort.h
        bb_segsort/bb/matrix/kv/bb_segsort.hpp
        bb_segsort/bb/matrix/bb_bin.hpp
        bb_segsort/bb/segments/k/bb_comput_l.hpp
        bb_segsort/bb/segments/k/bb_comput_s.hpp
        bb_segsort/bb/segments/k/bb_segsort.h
        bb_segsort/bb/segments/k/bb_segsort.hpp
        bb_segsort/bb/segments/kv/bb_comput_l.hpp
        bb_segsort/bb/segments/kv/bb_comput_s.hpp
        bb_segsort/bb/segments/kv/bb_segsort.h
        bb_segsort/bb/segments/kv/bb_segsort.hpp
        bb_segsort/bb/segments/bb_bin.hpp
        bb_segsort/bb/bb_comput_l.hpp
        bb_segsort/bb/bb_comput_s.hpp
        bb_segsort/bb/bb_numeric_limits.hpp
        bb_segsort/bb/bb_util.hpp
        src/sort.h
        src/jcusegsort.h
        src/jcusegsort.cu)

add_library(${PROJECT_NAME} SHARED
        src/datatype.h
        src/key_sort_context.cpp
        src/key_sort_context.h
        src/key_sort_context.hpp
        src/key_value_sort_context.cpp
        src/key_value_sort_context.h
        src/key_value_sort_context.hpp
        src/sorting.cpp
        src/sorting.h
        src/sort.h
        src/jcusegsort.cpp
        src/jcusegsort.h)
target_link_libraries(${PROJECT_NAME} ${PROJECT_NAME}-cuda)

set_target_properties(${PROJECT_NAME}
        PROPERTIES OUTPUT_NAME ${PROJECT_NAME}-${PROJECT_VERSION}-${PROJECT_OS}-${PROJECT_ARCH})