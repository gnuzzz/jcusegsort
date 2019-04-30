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
#include "sort.h"
#include "sorting.h"
#include "key_sort_context.hpp"
#include "key_value_sort_context.hpp"
#include "bb/k/bb_context.h"
#include "bb/kv/bb_context.h"

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
