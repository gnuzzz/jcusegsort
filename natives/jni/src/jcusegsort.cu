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

#include <jni.h>

#include "bb/segments/kv/bb_segsort.hpp"
#include "bb/segments/k/bb_segsort.hpp"
#include "bb/matrix/kv/bb_segsort.hpp"
#include "bb/matrix/k/bb_segsort.hpp"

#include "datatype.h"


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

jint sort(jlong keys_ptr, jint key_type, jint rows, jint cols, jlong context_ptr) {
  switch (key_type) {
    case BOOLEAN: {
      bb::matrix::k::bb_segsort((unsigned char *) keys_ptr, rows, cols, (bb::k::SortContext<unsigned char> *) context_ptr);
      break;
    }
    case BYTE: {
      bb::matrix::k::bb_segsort((char *) keys_ptr, rows, cols, (bb::k::SortContext<char> *) context_ptr);
      break;
    }
    case CHAR: {
      bb::matrix::k::bb_segsort((unsigned short *) keys_ptr, rows, cols, (bb::k::SortContext<unsigned short> *) context_ptr);
      break;
    }
    case SHORT: {
      bb::matrix::k::bb_segsort((short *) keys_ptr, rows, cols, (bb::k::SortContext<short> *) context_ptr);
      break;
    }
    case INT: {
      bb::matrix::k::bb_segsort((int *) keys_ptr, rows, cols, (bb::k::SortContext<int> *) context_ptr);
      break;
    }
    case LONG: {
      bb::matrix::k::bb_segsort((long long int *) keys_ptr, rows, cols, (bb::k::SortContext<long long int> *) context_ptr);
      break;
    }
    case FLOAT: {
      bb::matrix::k::bb_segsort((float *) keys_ptr, rows, cols, (bb::k::SortContext<float> *) context_ptr);
      break;
    }
    case DOUBLE: {
      bb::matrix::k::bb_segsort((double *) keys_ptr, rows, cols, (bb::k::SortContext<double> *) context_ptr);
      break;
    }
    default:
      return JNI_EINVAL;
  }
  return JNI_OK;
}

jint sort(jlong keys_ptr, jint key_type, jint keys_length, jlong segments_ptr, jint segments_length, jlong context_ptr) {
  switch (key_type) {
    case BOOLEAN: {
      bb::segments::k::bb_segsort((unsigned char *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<unsigned char> *) context_ptr);
      break;
    }
    case BYTE: {
      bb::segments::k::bb_segsort((char *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<char> *) context_ptr);
      break;
    }
    case CHAR: {
      bb::segments::k::bb_segsort((unsigned short *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<unsigned short> *) context_ptr);
      break;
    }
    case SHORT: {
      bb::segments::k::bb_segsort((short *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<short> *) context_ptr);
      break;
    }
    case INT: {
      bb::segments::k::bb_segsort((int *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<int> *) context_ptr);
      break;
    }
    case LONG: {
      bb::segments::k::bb_segsort((long long int *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<long long int> *) context_ptr);
      break;
    }
    case FLOAT: {
      bb::segments::k::bb_segsort((float *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<float> *) context_ptr);
      break;
    }
    case DOUBLE: {
      bb::segments::k::bb_segsort((double *) keys_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::k::SortContext<double> *) context_ptr);
      break;
    }
    default:
      return JNI_EINVAL;
  }
  return JNI_OK;
}

jint sort(jlong keys_ptr, jint key_type, jlong values_ptr, jint value_type, jint rows, jint cols, jlong context_ptr) {
  switch (key_type) {
    case BOOLEAN: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((unsigned char *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned char, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case BYTE: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<char, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<char, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<char, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<char, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<char, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<char, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<char, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((char *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<char, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case CHAR: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((unsigned short *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<unsigned short, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case SHORT: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<short, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<short, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<short, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<short, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<short, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<short, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<short, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((short *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<short, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case INT: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<int, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<int, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<int, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<int, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<int, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<int, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<int, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((int *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<int, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case LONG: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((long long int *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<long long int, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case FLOAT: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<float, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<float, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<float, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<float, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<float, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<float, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<float, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((float *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<float, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case DOUBLE: {
      switch (value_type) {
        case BOOLEAN:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (unsigned char *) values_ptr, rows, cols, (bb::kv::SortContext<double, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (char *) values_ptr, rows, cols, (bb::kv::SortContext<double, char> *) context_ptr);
          break;
        case CHAR:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (unsigned short *) values_ptr, rows, cols, (bb::kv::SortContext<double, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (short *) values_ptr, rows, cols, (bb::kv::SortContext<double, short> *) context_ptr);
          break;
        case INT:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (int *) values_ptr, rows, cols, (bb::kv::SortContext<double, int> *) context_ptr);
          break;
        case LONG:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (long long int *) values_ptr, rows, cols, (bb::kv::SortContext<double, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (float *) values_ptr, rows, cols, (bb::kv::SortContext<double, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::matrix::kv::bb_segsort((double *) keys_ptr, (double *) values_ptr, rows, cols, (bb::kv::SortContext<double, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    default:
      return JNI_EINVAL;
  }
  return JNI_OK;
}

jint sort(jlong keys_ptr, jint key_type, jlong values_ptr, jint value_type, jint keys_length, jlong segments_ptr, jint segments_length, jlong context_ptr) {
  switch (key_type) {
    case BOOLEAN: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((unsigned char *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned char, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case BYTE: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((char *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<char, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case CHAR: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((unsigned short *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<unsigned short, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case SHORT: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((short *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<short, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case INT: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((int *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<int, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case LONG: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((long long int *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<long long int, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case FLOAT: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((float *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<float, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    case DOUBLE: {
      switch (value_type) {
        case BOOLEAN:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (unsigned char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, unsigned char> *) context_ptr);
          break;
        case BYTE:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (char *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, char> *) context_ptr);
          break;
        case CHAR:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (unsigned short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, unsigned short> *) context_ptr);
          break;
        case SHORT:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (short *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, short> *) context_ptr);
          break;
        case INT:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, int> *) context_ptr);
          break;
        case LONG:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (long long int *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, long long int> *) context_ptr);
          break;
        case FLOAT:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (float *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, float> *) context_ptr);
          break;
        case DOUBLE:
          bb::segments::kv::bb_segsort((double *) keys_ptr, (double *) values_ptr, keys_length, (int *) segments_ptr, segments_length, (bb::kv::SortContext<double, double> *) context_ptr);
          break;
        default:
          return JNI_EINVAL;
      }
      break;
    }
    default:
      return JNI_EINVAL;
  }
  return JNI_OK;
}