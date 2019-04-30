#include <jni.h>

#ifndef _H_SORT
#define _H_SORT

jint sort(jlong keys_ptr, jint key_type, jint rows, jint cols, jlong context_ptr);

jint sort(jlong keys_ptr, jint key_type, jint keys_length, jlong segments_ptr, jint segments_length, jlong context_ptr);

jint sort(jlong keys_ptr, jint key_type, jlong values_ptr, jint value_type, jint rows, jint cols, jlong context_ptr);

jint sort(jlong keys_ptr, jint key_type, jlong values_ptr, jint value_type, jint keys_length, jlong segments_ptr, jint segments_length, jlong context_ptr);

#endif