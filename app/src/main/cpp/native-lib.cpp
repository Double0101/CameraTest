//
// Created by double on 17-8-23.
//
#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_sjgsu_ai_cameratest_TextFragment_getString(JNIEnv *env, jobject thiz)
{
    return env->NewStringUTF("Hello world");
}



