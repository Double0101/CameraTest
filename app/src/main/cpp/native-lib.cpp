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

jbyteArray
Java_com_sjgsu_ai_cameratest_CameraSurface(JNIEnv *envm, jobject thiz, jbyteArray yuv, jint width, jint height) {
    int total = 3 * width * height;
    char[] rgb = new char[total];
    char Y, Cb = 0, Cr = 0;
    int index = 0;
    char R, G, B;

    for (int y = 0; y < height; ++y) {
    	for (int x = 0; x < width; ++x) {
    		Y = yuv[y * width + x];
    		if (Y < 0) Y += 255;

    		if ((x & 1) == 0) {
    			Cr = yuv[(y >> 1) * (width) + x + total];
    			Cb = yuv[(y >> 1) * (width) + x + total + 1];

    			if (Cb < 0) Cb += 127; else Cb -= 128;
    			if (Cr < 0) Cr += 127; else Cr -= 128;
    		}

    		R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
    		G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
    		B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);

    		if (R < 0) R = 0; else if (R > 255) R = 255;
            if (G < 0) G = 0; else if (G > 255) G = 255;
           	if (B < 0) B = 0; else if (B > 255) B = 255;

            rgb[index++] = B;
            rgb[index++] = G;
            rgb[index++] = R;
        }
    }

    cv::Mat mat(width, height, )

}

}



