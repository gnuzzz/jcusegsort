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

#ifndef _H_UTIL
#define _H_UTIL

#include <jni.h>

jint init(JNIEnv *env);

bool init(JNIEnv *env, jclass& cls, const char *name);
bool init(JNIEnv *env, jclass cls, jfieldID& field, const char *name, const char *signature);

jlong getNativePointer(JNIEnv *env, jobject obj);
void setNativePointer(JNIEnv *env, jobject obj, jlong pointer);

#endif
