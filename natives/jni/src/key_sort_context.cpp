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

#include "key_sort_context.h"
#include "key_sort_context.hpp"
#include "bb/k/bb_context.h"
#include "jcusegsort.h"
#include "datatype.h"

jlong create_context(jint key_type, jint keys_length, jint segments_length) {
  jlong context_ptr;
  switch (key_type) {
    case BOOLEAN:
      context_ptr = (jlong) new bb::k::SortContext<unsigned char>(keys_length, segments_length);
      break;
    case BYTE:
      context_ptr = (jlong) new bb::k::SortContext<char>(keys_length, segments_length);
      break;
    case CHAR:
      context_ptr = (jlong) new bb::k::SortContext<unsigned short>(keys_length, segments_length);
      break;
    case SHORT:
      context_ptr = (jlong) new bb::k::SortContext<short>(keys_length, segments_length);
      break;
    case INT:
      context_ptr = (jlong) new bb::k::SortContext<int>(keys_length, segments_length);
      break;
    case LONG:
      context_ptr = (jlong) new bb::k::SortContext<long long int>(keys_length, segments_length);
      break;
    case FLOAT:
      context_ptr = (jlong) new bb::k::SortContext<float>(keys_length, segments_length);
      break;
    case DOUBLE:
      context_ptr = (jlong) new bb::k::SortContext<double>(keys_length, segments_length);
      break;
    default:
      context_ptr = 0;
  }
  return context_ptr;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_KeySortContext
 * Method:    init
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_KeySortContext_init
    (JNIEnv *env, jobject context, jint key_type, jint keys_length, jint segments_length)
{
  jlong context_ptr = create_context(key_type, keys_length, segments_length);
  if (context_ptr == 0) return JNI_EINVAL;
  setNativePointer(env, context, context_ptr);
  return JNI_OK;
}

/*
 * Class:     ru_albemuth_jcuda_jcusegsort_KeySortContext
 * Method:    free
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ru_albemuth_jcuda_jcusegsort_KeySortContext_free
    (JNIEnv *env, jobject context)
{
  auto *context_ptr = (bb::k::SortContext<int> *) getNativePointer(env, context);
  if (context_ptr != nullptr) {
    delete context_ptr;
    setNativePointer(env, context, 0);
  }
  return JNI_OK;
}
