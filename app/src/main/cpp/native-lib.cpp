//
// Created by double on 17-8-23.
//
#define DOUBLE_TAG "JNIMSG"

#include <jni.h>
#include <string>
#include <cmath>
#include <vector>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#include "npddetect.h"

using namespace cv;
using namespace std;

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

bool isEmbeded(Rect &x, Rect &y)
{
	int sx = x.area();
	int sy = y.area();
	return (double)(x & y).area() / (sx > sy ? sy : sx) > 0.5;
}

void merge(vector<int> &Xs, vector<int> &Ys, vector<int> &Ss, vector<float> &Scores, vector<Rect>  &groups)
{
    for (int i = 0; i != Xs.size(); ++i)
	    groups.push_back(Rect(Xs[i], Ys[i], Ss[i], Ss[i]));

    vector<int> flag(groups.size(), true);
    for (int i = 0; i != groups.size(); ++i)
	    for (int j = i + 1; j != groups.size(); ++j)
		    if (isEmbeded(groups.at(i), groups.at(j)))
			    flag.at(Scores[i] > Scores[j] ? j : i) = false;
    int k = 0;
    for (int i = 0; i != groups.size(); ++i)
	    if (flag[i]) groups[k++] = groups[i];
    groups.resize(k);
    return ;
}

jintArray
Java_com_sjgsu_ai_cameratest_CameraSurface_testDetect(JNIEnv *env, jobject thiz, jbyteArray data, jint width, jint height, jstring modelpath) {
    int bufLen = (int) ceil(width / 16) * 16;
    bufLen *= height;

    unsigned char* yuv = new unsigned char[bufLen];
    env->GetByteArrayRegion(data, 0, bufLen, reinterpret_cast<jbyte*>(yuv));

    cv::Mat img;
    img.create(height, width, CV_8UC1);
    memcpy(img.data, yuv, bufLen * sizeof(unsigned char));

    npd::npddetect npd;
    const char* path = env->GetStringUTFChars(modelpath, 0);
    npd.load(path);
    //visit the whole classifier

    int nt = 1;
    int nc = nt;
    int n;
    double t = (double)cvGetTickCount();
    while(nc-- > 0)
        n = npd.detect(img.data, img.cols, img.rows);
    t = ((double)cvGetTickCount() - t) / ((double)cvGetTickFrequency()*1000.) ;

    vector< int >& Xs = npd.getXs();
    vector< int >& Ys = npd.getYs();
    vector< int >& Ss = npd.getSs();
    vector< float >& Scores = npd.getScores();
    __android_log_print(ANDROID_LOG_INFO, DOUBLE_TAG, "%d\n", Xs.size());
    char buf[10];
    vector<Rect> groups;
    merge(Xs, Ys, Ss, Scores, groups);

    jintArray result = env->NewIntArray(4 * Xs.size());
    jint *h = new jint[4 * Xs.size()];
    for (int i = 0; i < Xs.size(); ++i)
    {
        h[4 * i] = Xs[i];
        h[4 * i + 1] = Ys[i];
        h[4 * i + 2] = Xs[i] + Ss[i];
        h[4 * i + 3] = Ys[i] + Ss[i];
    }
    env->SetIntArrayRegion(result, 0, 4 * Xs.size(), h);
    return result;
}

}



