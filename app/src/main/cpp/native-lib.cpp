//
// Created by double on 17-8-23.
//
#include <jni.h>
#include <string>
#include <opencv2/core.hpp>

extern "C" {
jstring
Java_com_sjgsu_ai_cameratest_TextFragment_getString(JNIEnv *env, jobject thiz)
{
    return env->NewStringUTF("Hello world");
}

jstring
Java_com_sjgsu_ai_cameratest_TextFragment_testOpenCV(JNIEnv *env, jobject thiz, jlong addrGray, jlong addrRgba) {
    cv::Rect();
    cv::Mat();
    std::string hello2 = "Opencv test success";
    return env->NewStringUTF(hello2.c_str());
}

}



