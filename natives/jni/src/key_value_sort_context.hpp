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

#ifndef _HPP_KEY_VALUE_SORT_CONTEXT
#define _HPP_KEY_VALUE_SORT_CONTEXT

#include <jni.h>

jlong create_context(jint key_type, jint value_type, jint keys_length, jint segments_length);

#endif