//
// Created by double on 17-8-23.
//
#define DOUBLE_TAG "JNIMSG"

#include <jni.h>
#include <time.h>
#include <string>
#include <cmath>
#include <vector>
#include <opencv2/opencv.hpp>
#include <android/log.h>
#include "npddetect.h"

#define PRINT_TIME(A, B) __android_log_print(ANDROID_LOG_INFO, DOUBLE_TAG, "STEP %s SPENT %lfsecond\n", A, (double) (B) / CLOCKS_PER_SEC)

using namespace cv;
using namespace std;

extern "C" {
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
Java_com_sjgsu_ai_cameratest_DetectThread_testDetect(JNIEnv *env, jobject thiz, jbyteArray data, jint width, jint height, jstring modelpath) {
    clock_t starttime, tmptime;
    starttime = tmptime = clock();
    int bufWid = (int) ceil(width / 16) * 16;
    jbyte *yuv = env->GetByteArrayElements(data, 0);
    cv::Mat img(height, width, CV_8UC1, (unsigned char *) yuv, bufWid);
    PRINT_TIME("get mat", clock() - tmptime);

// TEST
//    cv::imwrite("/storage/sdcard0/img_test.jpg", img);
    tmptime = clock();
    npd::npddetect npd;
    const char* path = env->GetStringUTFChars(modelpath, 0);
    npd.load(path);
    PRINT_TIME("read npd model", clock() - tmptime);

    //visit the whole classifier
    tmptime = clock();
    int nt = 1;
    int nc = nt;
    int n;
    double t = (double)cvGetTickCount();
    while(nc-- > 0)
        n = npd.detect(img.data, img.cols, img.rows);
    t = ((double)cvGetTickCount() - t) / ((double)cvGetTickFrequency()*1000.) ;
    PRINT_TIME("detect", clock() - tmptime);

    tmptime = clock();
    vector< int >& Xs = npd.getXs();
    vector< int >& Ys = npd.getYs();
    vector< int >& Ss = npd.getSs();
    vector< float >& Scores = npd.getScores();
    char buf[10];
    vector<Rect> groups;
    merge(Xs, Ys, Ss, Scores, groups);
    PRINT_TIME("merge result", clock() - tmptime);

    tmptime = clock();
    jintArray result = env->NewIntArray(4 * Xs.size());
    jint *h = new jint[4 * Xs.size()];
    for (int i = 0; i < Xs.size(); ++i)
    {
        h[4 * i] = Xs[i];
        h[4 * i + 1] = Ys[i];
        h[4 * i + 2] = Xs[i] + Ss[i];
        h[4 * i + 3] = Ys[i] + Ss[i];
        __android_log_print(ANDROID_LOG_INFO, DOUBLE_TAG, "%d %d %d %d\n", h[4 * i], h[4 * i + 1], h[4 * i + 2], h[4 * i + 3]);
    }
    env->SetIntArrayRegion(result, 0, 4 * Xs.size(), h);
    PRINT_TIME("copy result", clock() - tmptime);
    starttime = clock() - starttime;
    PRINT_TIME("total", starttime);
    return result;
}

}



