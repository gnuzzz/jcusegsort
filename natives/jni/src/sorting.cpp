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

#include "jcusegsort.h"
#include "sorting.h"
#include "datatype.h"
#include "key_sort_context.hpp"
#include "key_value_sort_context.hpp"
#include "bb/matrix/k/bb_segsort.h"
#include "bb/matrix/kv/bb_segsort.h"
#include "bb/segments/k/bb_segsort.h"
#include "bb/segments/kv/bb_segsort.h"

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

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;IILru/albemuth/jcuda/jcusegsort/KeySortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2IILru_albemuth_jcuda_jcusegsort_KeySortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint keys_length, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, 1, keys_length, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;II)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2II
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint keys_length)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong context_ptr = create_context(key_type, keys_length, 1);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, 1, keys_length, context_ptr);
  delete (bb::k::SortContext<int> *) context_ptr;
  return ret;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;IILru/albemuth/jcuda/jcusegsort/KeyValueSortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2IILru_albemuth_jcuda_jcusegsort_KeyValueSortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint keys_length, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, vals_ptr, value_type, 1, keys_length, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;II)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2II
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint keys_length)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong context_ptr = create_context(key_type, value_type, keys_length, 1);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, vals_ptr, value_type, 1, keys_length, context_ptr);
  delete (bb::kv::SortContext<int, int> *) context_ptr;
  return ret;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;IIILru/albemuth/jcuda/jcusegsort/KeySortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2IIILru_albemuth_jcuda_jcusegsort_KeySortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint rows, jint cols, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, rows, cols, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;III)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2III
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint rows, jint cols)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong context_ptr = create_context(key_type, rows * cols, rows);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, rows, cols, context_ptr);
  delete (bb::k::SortContext<int> *) context_ptr;
  return ret;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;IIILru/albemuth/jcuda/jcusegsort/KeyValueSortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2IIILru_albemuth_jcuda_jcusegsort_KeyValueSortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint rows, jint cols, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, vals_ptr, value_type, rows, cols, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;III)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2III
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint rows, jint cols)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong context_ptr = create_context(key_type, value_type, rows * cols, rows);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, vals_ptr, value_type, rows, cols, context_ptr);
  delete (bb::kv::SortContext<int, int> *) context_ptr;
  return ret;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;IILjcuda/driver/CUdeviceptr;ILru/albemuth/jcuda/jcusegsort/KeySortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2IILjcuda_driver_CUdeviceptr_2ILru_albemuth_jcuda_jcusegsort_KeySortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint keys_length, jobject segments, jint segments_length, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong segments_ptr = getNativePointer(env, segments);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, keys_length, segments_ptr, segments_length, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;IILjcuda/driver/CUdeviceptr;I)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2IILjcuda_driver_CUdeviceptr_2I
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jint keys_length, jobject segments, jint segments_length)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong segments_ptr = getNativePointer(env, segments);
  jlong context_ptr = create_context(key_type, keys_length, segments_length);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, keys_length, segments_ptr, segments_length, context_ptr);
  delete (bb::k::SortContext<int> *) context_ptr;
  return ret;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;IILjcuda/driver/CUdeviceptr;ILru/albemuth/jcuda/jcusegsort/KeyValueSortContext;)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2IILjcuda_driver_CUdeviceptr_2ILru_albemuth_jcuda_jcusegsort_KeyValueSortContext_2
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint keys_length, jobject segments, jint segments_length, jobject context)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong segments_ptr = getNativePointer(env, segments);
  jlong context_ptr = getNativePointer(env, context);
  return sort(keys_ptr, key_type, vals_ptr, value_type, keys_length, segments_ptr, segments_length, context_ptr);
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_Sorting
 * Method:    sortNative
 * Signature: (Ljcuda/driver/CUdeviceptr;ILjcuda/driver/CUdeviceptr;IILjcuda/driver/CUdeviceptr;I)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_Sorting_sortNative__Ljcuda_driver_CUdeviceptr_2ILjcuda_driver_CUdeviceptr_2IILjcuda_driver_CUdeviceptr_2I
    (JNIEnv *env, jclass cls, jobject keys, jint key_type, jobject vals, jint value_type, jint keys_length, jobject segments, jint segments_length)
{
  jlong keys_ptr = getNativePointer(env, keys);
  jlong vals_ptr = getNativePointer(env, vals);
  jlong segments_ptr = getNativePointer(env, segments);
  jlong context_ptr = create_context(key_type, value_type, keys_length, segments_length);
  if (context_ptr == 0) return JNI_EINVAL;
  jint ret = sort(keys_ptr, key_type, vals_ptr, value_type, keys_length, segments_ptr, segments_length, context_ptr);
  delete (bb::kv::SortContext<int, int> *) context_ptr;
  return ret;
}
