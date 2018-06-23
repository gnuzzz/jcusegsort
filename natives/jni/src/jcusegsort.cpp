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

#include <stdio.h>
#include <jni.h>

#include "jcusegsort.h"

jfieldID NativePointerObject_nativePointer;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
  JNIEnv *env = nullptr;
  if (jvm->GetEnv((void **)&env, JNI_VERSION_1_4)) {
    return JNI_ERR;
  }

  if (init(env) == JNI_ERR) return JNI_ERR;

  return JNI_VERSION_1_4;
}

jint init(JNIEnv *env) {
  jclass cls = NULL;

  // Obtain the fieldIDs of the NativePointerObject class
  if (!init(env, cls, "jcuda/NativePointerObject")) return JNI_ERR;
  if (!init(env, cls, NativePointerObject_nativePointer, "nativePointer", "J")) return JNI_ERR;

  return JNI_VERSION_1_4;
}

/**
 * Initialize the given jclass, and return whether
 * the initialization succeeded
 */
bool init(JNIEnv *env, jclass& cls, const char *name)
{
  cls = env->FindClass(name);
  if (cls == NULL)
  {
    return false;
  }
  return true;
}

/**
 * Initialize the specified field ID, and return whether
 * the initialization succeeded
 */
bool init(JNIEnv *env, jclass cls, jfieldID& field, const char *name, const char *signature)
{
  field = env->GetFieldID(cls, name, signature);
  if (field == NULL)
  {
    return false;
  }
  return true;
}

jlong getNativePointer(JNIEnv *env, jobject obj) {
  return env->GetLongField(obj, NativePointerObject_nativePointer);
}

void setNativePointer(JNIEnv *env, jobject obj, jlong pointer) {
  env->SetLongField(obj, NativePointerObject_nativePointer, pointer);
}
