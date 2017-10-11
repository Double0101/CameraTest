//
// Created by double on 17-8-23.
//
#include <jni.h>
#include <string>
#include <vector>
#include <opencv2/opencv.hpp>
#include "npddetect.h"

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
	return (double)(x & y).area() / (sx > sy ? sy : sx)  > 0.5;
}

void merge(vector<int> &Xs, vector<int> &Ys, vector<int> &Ss, vector<float> &Scores, vector<Rect>  &groups)
{
    for (int i = 0; i != Xs.size(); i++)
	    groups.push_back(Rect(Xs[i], Ys[i], Ss[i], Ss[i]));

    vector<int> flag(groups.size(), true);
    for (int i = 0; i != groups.size(); i++)
	    for (int j = i + 1; j != groups.size(); j++)
		    if (isEmbeded(groups.at(i), groups.at(j)))
			    flag.at(Scores[i] > Scores[j] ? j : i) = false;
    int k = 0;
    for (int i = 0; i != groups.size(); i++)
	    if (flag[i]) groups[k++] = groups[i];
    groups.resize(k);
    return ;
}

jintArray
Java_com_sjgsu_ai_cameratest_CameraSurface(JNIEnv *envm, jobject thiz, jbyteArray yuv, jint width, jint height, jstring modelpath) {
    int bufLen = width * height * 3 / 2;
    unsigned char* pYuvBuf = new unsigned char[bufLen];

    cv::Mat yuvImg;
    yuvImg.create(height * 3 / 2, width, CV_8UC1);
    cv::memcpy(yuvImg.data, pYuvBuf, bufLen * sizeof(unsigned char));
    cv::Mat img;
    cv::cvtColor(yuvImg, rgbImg, COLOR_YUV420sp2BGR);

    npd::npddetect npd;
    npd.load(modelpath);
    //visit the whole classifier

    int nt = 1;
    int nc = nt;
    int n;
    double t = (double)cvGetTickCount();
    while(nc-- > 0)
        n = npd.detect(img.data, img.cols, img.rows);
    t = ((double)cvGetTickCount() - t) / ((double)cvGetTickFrequency()*1000.) ;

    printf("Detect num: %d (%lf ms avg of %d test)\n", n, t/nt, nt);
    vector< int >& Xs = npd.getXs();
    vector< int >& Ys = npd.getYs();
    vector< int >& Ss = npd.getSs();
    vector< float >& Scores = npd.getScores();
    char buf[10];
    vector<Rect> groups;
    merge(Xs, Ys, Ss, Scores, groups);

    jintArray result = env->NewIntArray(4 * Xs.size());
    for (int i = 0; i < Xs.size(); ++i)
    {
        result[4 * i] = Xs[i];
        result[4 * i + 1] = Ys[i];
        result[4 * i + 2] = Xs[i] + Ss[i];
        result[4 * i + 3] = Ys[i] + Ss[i];
    }
    return result;
}

}



