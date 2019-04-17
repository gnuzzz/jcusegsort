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

#include "key_value_sort_context.h"
#include "key_value_sort_context.hpp"
#include "bb/kv/bb_context.hpp"
#include "jcusegsort.h"
#include "datatype.h"

jlong create_context(jint key_type, jint value_type, jint keys_length, jint segments_length) {
  jlong context_ptr;
  switch (key_type) {
    case BOOLEAN:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned char, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case BYTE:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<char, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<char, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<char, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<char, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<char, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<char, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<char, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<char, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case CHAR:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<unsigned short, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case SHORT:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<short, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<short, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<short, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<short, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<short, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<short, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<short, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<short, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case INT:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<int, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<int, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<int, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<int, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<int, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<int, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<int, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<int, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case LONG:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<long long int, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case FLOAT:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<float, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<float, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<float, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<float, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<float, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<float, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<float, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<float, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    case DOUBLE:
      switch (value_type) {
        case BOOLEAN:
          context_ptr = (jlong) new bb::kv::SortContext<double, unsigned char>(keys_length, segments_length);
          break;
        case BYTE:
          context_ptr = (jlong) new bb::kv::SortContext<double, char>(keys_length, segments_length);
          break;
        case CHAR:
          context_ptr = (jlong) new bb::kv::SortContext<double, unsigned short>(keys_length, segments_length);
          break;
        case SHORT:
          context_ptr = (jlong) new bb::kv::SortContext<double, short>(keys_length, segments_length);
          break;
        case INT:
          context_ptr = (jlong) new bb::kv::SortContext<double, int>(keys_length, segments_length);
          break;
        case LONG:
          context_ptr = (jlong) new bb::kv::SortContext<double, long long int>(keys_length, segments_length);
          break;
        case FLOAT:
          context_ptr = (jlong) new bb::kv::SortContext<double, float>(keys_length, segments_length);
          break;
        case DOUBLE:
          context_ptr = (jlong) new bb::kv::SortContext<double, double>(keys_length, segments_length);
          break;
        default:
          context_ptr = 0;
          break;
      }
      break;
    default:
      context_ptr = 0;
      break;
  }
  return context_ptr;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_KeyValueSortContext
 * Method:    init
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_KeyValueSortContext_init
    (JNIEnv *env, jobject context, jint key_type, jint value_type, jint keys_length, jint segments_length)
{
  jlong context_ptr = create_context(key_type, value_type, keys_length, segments_length);
  if (context_ptr == 0) return JNI_EINVAL;
  setNativePointer(env, context, context_ptr);
  return JNI_OK;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_KeyValueSortContext
 * Method:    free
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_KeyValueSortContext_free
    (JNIEnv *env, jobject context)
{
  auto *context_ptr = (bb::kv::SortContext<int, int> *) getNativePointer(env, context);
  if (context_ptr != nullptr) {
    delete context_ptr;
    setNativePointer(env, context, 0);
  }
  return JNI_OK;
}
