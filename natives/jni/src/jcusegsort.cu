/*
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, version 2.1
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License, version 2.1, for more details.
*
*   You should have received a copy of the GNU General Public License
*
*/

#include "bb/segments/kv/bb_segsort.hpp"
#include "bb/segments/k/bb_segsort.hpp"
#include "bb/matrix/kv/bb_segsort.hpp"
#include "bb/matrix/k/bb_segsort.hpp"

#include "jcusegsort.h"

template <class K>
int sort_matrix(K* key, int rows, int cols, const bb::k::SortContext<K>* context) {
  cudaError_t err;
  K    *key_d;

  err = cudaMalloc((void**)&key_d, sizeof(K) * rows * cols);
  CUDA_CHECK(err, "matrix: alloc key_d");

  err = cudaMemcpy(key_d, key, sizeof(K) * rows * cols, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "matrix: copy to key_d");

  bb::matrix::k::bb_segsort(key_d, rows, cols, context);

  err = cudaMemcpy(key, key_d, sizeof(K) * rows * cols, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "matrix: copy to key_d");

  err = cudaFree(key_d);
  CUDA_CHECK(err, "matrix: free key_d");

  return 0;
}

template <class K, class V>
int sort_matrix(K* key, V* val, int rows, int cols, const bb::kv::SortContext<K, V>* context) {
  cudaError_t err;
  K    *key_d;
  V    *val_d;

  err = cudaMalloc((void**)&key_d, sizeof(K) * rows * cols);
  CUDA_CHECK(err, "matrix: alloc key_d");
  err = cudaMalloc((void**)&val_d, sizeof(V) * rows * cols);
  CUDA_CHECK(err, "matrix: alloc val_d");

  err = cudaMemcpy(key_d, key, sizeof(K) * rows * cols, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "matrix: copy to key_d");
  err = cudaMemcpy(val_d, val, sizeof(V) * rows * cols, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "matrix: copy to val_d");

  bb::matrix::kv::bb_segsort(key_d, val_d, rows, cols, context);

  err = cudaMemcpy(key, key_d, sizeof(K) * rows * cols, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "matrix: copy to key_d");
  err = cudaMemcpy(val, val_d, sizeof(K) * rows * cols, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "matrix: copy to val_d");

  err = cudaFree(key_d);
  CUDA_CHECK(err, "matrix: free key_d");
  err = cudaFree(val_d);
  CUDA_CHECK(err, "matrix: free val_d");

  return 0;
}

template <class K>
int sort_matrix(K* key, int rows, int cols) {
  bb::k::SortContext<K> context_k(rows * cols, rows);
  return sort_matrix(key, rows, cols, &context_k);
}

template <class K, class V>
int sort_matrix(K* key, V* val, int rows, int cols) {
  bb::kv::SortContext<K, V> context_kv(rows * cols, rows);
  return sort_matrix(key, val, rows, cols, &context_kv);
}


template <class K>
int sort_segments(K* key, int* seg, int n, int length, const bb::k::SortContext<K>* context) {
  cudaError_t err;
  K    *key_d;
  int  *seg_d;

  err = cudaMalloc((void**)&key_d, sizeof(K)*n);
  CUDA_CHECK(err, "segments: alloc key_d");
  err = cudaMalloc((void**)&seg_d, sizeof(int)*length);
  CUDA_CHECK(err, "segments: alloc seg_d");

  err = cudaMemcpy(key_d, key, sizeof(K)*n, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "segments: copy to key_d");
  err = cudaMemcpy(seg_d, seg, sizeof(int)*length, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "segments: copy to seg_d");

  bb::segments::k::bb_segsort(key_d, n, seg_d, length, context);

  err = cudaMemcpy(key, key_d, sizeof(K)*n, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "segments: copy to key_d");

  err = cudaFree(key_d);
  CUDA_CHECK(err, "segments: free key_d");
  err = cudaFree(seg_d);
  CUDA_CHECK(err, "segments: free seg_d");

  return 0;
}

template <class K, class V>
int sort_segments(K* key, V* val, int* seg, int n, int length, const bb::kv::SortContext<K, V>* context) {
  cudaError_t err;
  K    *key_d;
  V    *val_d;
  int  *seg_d;

  err = cudaMalloc((void**)&key_d, sizeof(K)*n);
  CUDA_CHECK(err, "segments: alloc key_d");
  err = cudaMalloc((void**)&val_d, sizeof(V)*n);
  CUDA_CHECK(err, "segments: alloc val_d");
  err = cudaMalloc((void**)&seg_d, sizeof(int)*length);
  CUDA_CHECK(err, "segments: alloc seg_d");

  err = cudaMemcpy(key_d, key, sizeof(K)*n, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "segments: copy to key_d");
  err = cudaMemcpy(val_d, val, sizeof(V)*n, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "segments: copy to val_d");
  err = cudaMemcpy(seg_d, seg, sizeof(int)*length, cudaMemcpyHostToDevice);
  CUDA_CHECK(err, "segments: copy to seg_d");

  bb::segments::kv::bb_segsort(key_d, val_d, n, seg_d, length, context);

  err = cudaMemcpy(key, key_d, sizeof(K)*n, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "segments: copy to key_d");
  err = cudaMemcpy(val, val_d, sizeof(V)*n, cudaMemcpyDeviceToHost);
  CUDA_CHECK(err, "segments: copy to val_d");

  err = cudaFree(key_d);
  CUDA_CHECK(err, "segments: free key_d");
  err = cudaFree(val_d);
  CUDA_CHECK(err, "segments: free val_d");
  err = cudaFree(seg_d);
  CUDA_CHECK(err, "segments: free seg_d");

  return 0;
}

template <class K>
int sort_segments(K* key, int* seg, int n, int length) {
  bb::k::SortContext<K> context_k(n, length);
  return sort_segments(key, seg, n, length, &context_k);
}

template <class K, class V>
int sort_segments(K* key, V* val, int* seg, int n, int length) {
  bb::kv::SortContext<K, V> context_kv(n, length);
  return sort_segments(key, val, seg, n, length, &context_kv);
}

///////////////// sort keys matrix /////////////////////////////////////////////////
int sort_matrix_unsigned_char(unsigned char *key, int rows, int cols) {
  return sort_matrix<unsigned char>(key, rows, cols);
}

int sort_matrix_char(char *key, int rows, int cols) {
  return sort_matrix<char>(key, rows, cols);
}

int sort_matrix_unsigned_short(unsigned short *key, int rows, int cols) {
  return sort_matrix<unsigned short>(key, rows, cols);
}

int sort_matrix_short(short *key, int rows, int cols) {
  return sort_matrix<short>(key, rows, cols);
}

int sort_matrix_int(int *key, int rows, int cols) {
  return sort_matrix<int>(key, rows, cols);
}

int sort_matrix_long_long_int(long long int *key, int rows, int cols) {
  return sort_matrix<long long int>(key, rows, cols);
}

int sort_matrix_float(float *key, int rows, int cols) {
  return sort_matrix<float>(key, rows, cols);
}

int sort_matrix_double(double *key, int rows, int cols) {
  return sort_matrix<double>(key, rows, cols);
}

///////////////// sort key-value pairs matrix ///////////////////////////////////////////
int sort_matrix_unsigned_char_unsigned_char(unsigned char *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<unsigned char, unsigned char>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_char(unsigned char *key, char *val, int rows, int cols) {
  return sort_matrix<unsigned char, char>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_unsigned_short(unsigned char *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<unsigned char, unsigned short>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_short(unsigned char *key, short *val, int rows, int cols) {
  return sort_matrix<unsigned char, short>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_int(unsigned char *key, int *val, int rows, int cols) {
  return sort_matrix<unsigned char, int>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_long_long_int(unsigned char *key, long long int *val, int rows, int cols) {
  return sort_matrix<unsigned char, long long int>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_float(unsigned char *key, float *val, int rows, int cols) {
  return sort_matrix<unsigned char, float>(key, val, rows, cols);
}

int sort_matrix_unsigned_char_double(unsigned char *key, double *val, int rows, int cols) {
  return sort_matrix<unsigned char, double>(key, val, rows, cols);
}

int sort_matrix_char_unsigned_char(char *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<char, unsigned char>(key, val, rows, cols);
}

int sort_matrix_char_char(char *key, char *val, int rows, int cols) {
  return sort_matrix<char, char>(key, val, rows, cols);
}

int sort_matrix_char_unsigned_short(char *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<char, unsigned short>(key, val, rows, cols);
}

int sort_matrix_char_short(char *key, short *val, int rows, int cols) {
  return sort_matrix<char, short>(key, val, rows, cols);
}

int sort_matrix_char_int(char *key, int *val, int rows, int cols) {
  return sort_matrix<char, int>(key, val, rows, cols);
}

int sort_matrix_char_long_long_int(char *key, long long int *val, int rows, int cols) {
  return sort_matrix<char, long long int>(key, val, rows, cols);
}

int sort_matrix_char_float(char *key, float *val, int rows, int cols) {
  return sort_matrix<char, float>(key, val, rows, cols);
}

int sort_matrix_char_double(char *key, double *val, int rows, int cols) {
  return sort_matrix<char, double>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_unsigned_char(unsigned short *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<unsigned short, unsigned char>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_char(unsigned short *key, char *val, int rows, int cols) {
  return sort_matrix<unsigned short, char>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_unsigned_short(unsigned short *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<unsigned short, unsigned short>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_short(unsigned short *key, short *val, int rows, int cols) {
  return sort_matrix<unsigned short, short>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_int(unsigned short *key, int *val, int rows, int cols) {
  return sort_matrix<unsigned short, int>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_long_long_int(unsigned short *key, long long int *val, int rows, int cols) {
  return sort_matrix<unsigned short, long long int>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_float(unsigned short *key, float *val, int rows, int cols) {
  return sort_matrix<unsigned short, float>(key, val, rows, cols);
}

int sort_matrix_unsigned_short_double(unsigned short *key, double *val, int rows, int cols) {
  return sort_matrix<unsigned short, double>(key, val, rows, cols);
}

int sort_matrix_short_unsigned_char(short *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<short, unsigned char>(key, val, rows, cols);
}

int sort_matrix_short_char(short *key, char *val, int rows, int cols) {
  return sort_matrix<short, char>(key, val, rows, cols);
}

int sort_matrix_short_unsigned_short(short *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<short, unsigned short>(key, val, rows, cols);
}

int sort_matrix_short_short(short *key, short *val, int rows, int cols) {
  return sort_matrix<short, short>(key, val, rows, cols);
}

int sort_matrix_short_int(short *key, int *val, int rows, int cols) {
  return sort_matrix<short, int>(key, val, rows, cols);
}

int sort_matrix_short_long_long_int(short *key, long long int *val, int rows, int cols) {
  return sort_matrix<short, long long int>(key, val, rows, cols);
}

int sort_matrix_short_float(short *key, float *val, int rows, int cols) {
  return sort_matrix<short, float>(key, val, rows, cols);
}

int sort_matrix_short_double(short *key, double *val, int rows, int cols) {
  return sort_matrix<short, double>(key, val, rows, cols);
}

int sort_matrix_int_unsigned_char(int *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<int, unsigned char>(key, val, rows, cols);
}

int sort_matrix_int_char(int *key, char *val, int rows, int cols) {
  return sort_matrix<int, char>(key, val, rows, cols);
}

int sort_matrix_int_unsigned_short(int *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<int, unsigned short>(key, val, rows, cols);
}

int sort_matrix_int_short(int *key, short *val, int rows, int cols) {
  return sort_matrix<int, short>(key, val, rows, cols);
}

int sort_matrix_int_int(int *key, int *val, int rows, int cols) {
  return sort_matrix<int, int>(key, val, rows, cols);
}

int sort_matrix_int_long_long_int(int *key, long long int *val, int rows, int cols) {
  return sort_matrix<int, long long int>(key, val, rows, cols);
}

int sort_matrix_int_float(int *key, float *val, int rows, int cols) {
  return sort_matrix<int, float>(key, val, rows, cols);
}

int sort_matrix_int_double(int *key, double *val, int rows, int cols) {
  return sort_matrix<int, double>(key, val, rows, cols);
}

int sort_matrix_long_long_int_unsigned_char(long long int *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<long long int, unsigned char>(key, val, rows, cols);
}

int sort_matrix_long_long_int_char(long long int *key, char *val, int rows, int cols) {
  return sort_matrix<long long int, char>(key, val, rows, cols);
}

int sort_matrix_long_long_int_unsigned_short(long long int *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<long long int, unsigned short>(key, val, rows, cols);
}

int sort_matrix_long_long_int_short(long long int *key, short *val, int rows, int cols) {
  return sort_matrix<long long int, short>(key, val, rows, cols);
}

int sort_matrix_long_long_int_int(long long int *key, int *val, int rows, int cols) {
  return sort_matrix<long long int, int>(key, val, rows, cols);
}

int sort_matrix_long_long_int_long_long_int(long long int *key, long long int *val, int rows, int cols) {
  return sort_matrix<long long int, long long int>(key, val, rows, cols);
}

int sort_matrix_long_long_int_float(long long int *key, float *val, int rows, int cols) {
  return sort_matrix<long long int, float>(key, val, rows, cols);
}

int sort_matrix_long_long_int_double(long long int *key, double *val, int rows, int cols) {
  return sort_matrix<long long int, double>(key, val, rows, cols);
}

int sort_matrix_float_unsigned_char(float *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<float, unsigned char>(key, val, rows, cols);
}

int sort_matrix_float_char(float *key, char *val, int rows, int cols) {
  return sort_matrix<float, char>(key, val, rows, cols);
}

int sort_matrix_float_unsigned_short(float *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<float, unsigned short>(key, val, rows, cols);
}

int sort_matrix_float_short(float *key, short *val, int rows, int cols) {
  return sort_matrix<float, short>(key, val, rows, cols);
}

int sort_matrix_float_int(float *key, int *val, int rows, int cols) {
  return sort_matrix<float, int>(key, val, rows, cols);
}

int sort_matrix_float_long_long_int(float *key, long long int *val, int rows, int cols) {
  return sort_matrix<float, long long int>(key, val, rows, cols);
}

int sort_matrix_float_float(float *key, float *val, int rows, int cols) {
  return sort_matrix<float, float>(key, val, rows, cols);
}

int sort_matrix_float_double(float *key, double *val, int rows, int cols) {
  return sort_matrix<float, double>(key, val, rows, cols);
}

int sort_matrix_double_unsigned_char(double *key, unsigned char *val, int rows, int cols) {
  return sort_matrix<double, unsigned char>(key, val, rows, cols);
}

int sort_matrix_double_char(double *key, char *val, int rows, int cols) {
  return sort_matrix<double, char>(key, val, rows, cols);
}

int sort_matrix_double_unsigned_short(double *key, unsigned short *val, int rows, int cols) {
  return sort_matrix<double, unsigned short>(key, val, rows, cols);
}

int sort_matrix_double_short(double *key, short *val, int rows, int cols) {
  return sort_matrix<double, short>(key, val, rows, cols);
}

int sort_matrix_double_int(double *key, int *val, int rows, int cols) {
  return sort_matrix<double, int>(key, val, rows, cols);
}

int sort_matrix_double_long_long_int(double *key, long long int *val, int rows, int cols) {
  return sort_matrix<double, long long int>(key, val, rows, cols);
}

int sort_matrix_double_float(double *key, float *val, int rows, int cols) {
  return sort_matrix<double, float>(key, val, rows, cols);
}

int sort_matrix_double_double(double *key, double *val, int rows, int cols) {
  return sort_matrix<double, double>(key, val, rows, cols);
}

///////////////// sort keys by segments /////////////////////////////////////////////////
int sort_segments_unsigned_char(unsigned char *key, int* seg, int n, int length) {
  return sort_segments<unsigned char>(key, seg, n, length);
}

int sort_segments_char(char *key, int* seg, int n, int length) {
  return sort_segments<char>(key, seg, n, length);
}

int sort_segments_unsigned_short(unsigned short *key, int* seg, int n, int length) {
  return sort_segments<unsigned short>(key, seg, n, length);
}

int sort_segments_short(short *key, int* seg, int n, int length) {
  return sort_segments<short>(key, seg, n, length);
}

int sort_segments_int(int *key, int* seg, int n, int length) {
  return sort_segments<int>(key, seg, n, length);
}

int sort_segments_long_long_int(long long int *key, int* seg, int n, int length) {
  return sort_segments<long long int>(key, seg, n, length);
}

int sort_segments_float(float *key, int* seg, int n, int length) {
  return sort_segments<float>(key, seg, n, length);
}

int sort_segments_double(double *key, int* seg, int n, int length) {
  return sort_segments<double>(key, seg, n, length);
}

///////////////// sort key-value pairs by segments ///////////////////////////////////////////
int sort_segments_unsigned_char_unsigned_char(unsigned char *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, unsigned char>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_char(unsigned char *key, char *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, char>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_unsigned_short(unsigned char *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, unsigned short>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_short(unsigned char *key, short *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, short>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_int(unsigned char *key, int *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, int>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_long_long_int(unsigned char *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, long long int>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_float(unsigned char *key, float *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, float>(key, val, seg, n, length);
}

int sort_segments_unsigned_char_double(unsigned char *key, double *val, int *seg, int n, int length) {
  return sort_segments<unsigned char, double>(key, val, seg, n, length);
}

int sort_segments_char_unsigned_char(char *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<char, unsigned char>(key, val, seg, n, length);
}

int sort_segments_char_char(char *key, char *val, int *seg, int n, int length) {
  return sort_segments<char, char>(key, val, seg, n, length);
}

int sort_segments_char_unsigned_short(char *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<char, unsigned short>(key, val, seg, n, length);
}

int sort_segments_char_short(char *key, short *val, int *seg, int n, int length) {
  return sort_segments<char, short>(key, val, seg, n, length);
}

int sort_segments_char_int(char *key, int *val, int *seg, int n, int length) {
  return sort_segments<char, int>(key, val, seg, n, length);
}

int sort_segments_char_long_long_int(char *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<char, long long int>(key, val, seg, n, length);
}

int sort_segments_char_float(char *key, float *val, int *seg, int n, int length) {
  return sort_segments<char, float>(key, val, seg, n, length);
}

int sort_segments_char_double(char *key, double *val, int *seg, int n, int length) {
  return sort_segments<char, double>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_unsigned_char(unsigned short *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, unsigned char>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_char(unsigned short *key, char *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, char>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_unsigned_short(unsigned short *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, unsigned short>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_short(unsigned short *key, short *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, short>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_int(unsigned short *key, int *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, int>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_long_long_int(unsigned short *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, long long int>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_float(unsigned short *key, float *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, float>(key, val, seg, n, length);
}

int sort_segments_unsigned_short_double(unsigned short *key, double *val, int *seg, int n, int length) {
  return sort_segments<unsigned short, double>(key, val, seg, n, length);
}

int sort_segments_short_unsigned_char(short *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<short, unsigned char>(key, val, seg, n, length);
}

int sort_segments_short_char(short *key, char *val, int *seg, int n, int length) {
  return sort_segments<short, char>(key, val, seg, n, length);
}

int sort_segments_short_unsigned_short(short *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<short, unsigned short>(key, val, seg, n, length);
}

int sort_segments_short_short(short *key, short *val, int *seg, int n, int length) {
  return sort_segments<short, short>(key, val, seg, n, length);
}

int sort_segments_short_int(short *key, int *val, int *seg, int n, int length) {
  return sort_segments<short, int>(key, val, seg, n, length);
}

int sort_segments_short_long_long_int(short *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<short, long long int>(key, val, seg, n, length);
}

int sort_segments_short_float(short *key, float *val, int *seg, int n, int length) {
  return sort_segments<short, float>(key, val, seg, n, length);
}

int sort_segments_short_double(short *key, double *val, int *seg, int n, int length) {
  return sort_segments<short, double>(key, val, seg, n, length);
}

int sort_segments_int_unsigned_char(int *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<int, unsigned char>(key, val, seg, n, length);
}

int sort_segments_int_char(int *key, char *val, int *seg, int n, int length) {
  return sort_segments<int, char>(key, val, seg, n, length);
}

int sort_segments_int_unsigned_short(int *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<int, unsigned short>(key, val, seg, n, length);
}

int sort_segments_int_short(int *key, short *val, int *seg, int n, int length) {
  return sort_segments<int, short>(key, val, seg, n, length);
}

int sort_segments_int_int(int *key, int *val, int *seg, int n, int length) {
  return sort_segments<int, int>(key, val, seg, n, length);
}

int sort_segments_int_long_long_int(int *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<int, long long int>(key, val, seg, n, length);
}

int sort_segments_int_float(int *key, float *val, int *seg, int n, int length) {
  return sort_segments<int, float>(key, val, seg, n, length);
}

int sort_segments_int_double(int *key, double *val, int *seg, int n, int length) {
  return sort_segments<int, double>(key, val, seg, n, length);
}

int sort_segments_long_long_int_unsigned_char(long long int *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<long long int, unsigned char>(key, val, seg, n, length);
}

int sort_segments_long_long_int_char(long long int *key, char *val, int *seg, int n, int length) {
  return sort_segments<long long int, char>(key, val, seg, n, length);
}

int sort_segments_long_long_int_unsigned_short(long long int *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<long long int, unsigned short>(key, val, seg, n, length);
}

int sort_segments_long_long_int_short(long long int *key, short *val, int *seg, int n, int length) {
  return sort_segments<long long int, short>(key, val, seg, n, length);
}

int sort_segments_long_long_int_int(long long int *key, int *val, int *seg, int n, int length) {
  return sort_segments<long long int, int>(key, val, seg, n, length);
}

int sort_segments_long_long_int_long_long_int(long long int *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<long long int, long long int>(key, val, seg, n, length);
}

int sort_segments_long_long_int_float(long long int *key, float *val, int *seg, int n, int length) {
  return sort_segments<long long int, float>(key, val, seg, n, length);
}

int sort_segments_long_long_int_double(long long int *key, double *val, int *seg, int n, int length) {
  return sort_segments<long long int, double>(key, val, seg, n, length);
}

int sort_segments_float_unsigned_char(float *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<float, unsigned char>(key, val, seg, n, length);
}

int sort_segments_float_char(float *key, char *val, int *seg, int n, int length) {
  return sort_segments<float, char>(key, val, seg, n, length);
}

int sort_segments_float_unsigned_short(float *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<float, unsigned short>(key, val, seg, n, length);
}

int sort_segments_float_short(float *key, short *val, int *seg, int n, int length) {
  return sort_segments<float, short>(key, val, seg, n, length);
}

int sort_segments_float_int(float *key, int *val, int *seg, int n, int length) {
  return sort_segments<float, int>(key, val, seg, n, length);
}

int sort_segments_float_long_long_int(float *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<float, long long int>(key, val, seg, n, length);
}

int sort_segments_float_float(float *key, float *val, int *seg, int n, int length) {
  return sort_segments<float, float>(key, val, seg, n, length);
}

int sort_segments_float_double(float *key, double *val, int *seg, int n, int length) {
  return sort_segments<float, double>(key, val, seg, n, length);
}

int sort_segments_double_unsigned_char(double *key, unsigned char *val, int *seg, int n, int length) {
  return sort_segments<double, unsigned char>(key, val, seg, n, length);
}

int sort_segments_double_char(double *key, char *val, int *seg, int n, int length) {
  return sort_segments<double, char>(key, val, seg, n, length);
}

int sort_segments_double_unsigned_short(double *key, unsigned short *val, int *seg, int n, int length) {
  return sort_segments<double, unsigned short>(key, val, seg, n, length);
}

int sort_segments_double_short(double *key, short *val, int *seg, int n, int length) {
  return sort_segments<double, short>(key, val, seg, n, length);
}

int sort_segments_double_int(double *key, int *val, int *seg, int n, int length) {
  return sort_segments<double, int>(key, val, seg, n, length);
}

int sort_segments_double_long_long_int(double *key, long long int *val, int *seg, int n, int length) {
  return sort_segments<double, long long int>(key, val, seg, n, length);
}

int sort_segments_double_float(double *key, float *val, int *seg, int n, int length) {
  return sort_segments<double, float>(key, val, seg, n, length);
}

int sort_segments_double_double(double *key, double *val, int *seg, int n, int length) {
  return sort_segments<double, double>(key, val, seg, n, length);
}
