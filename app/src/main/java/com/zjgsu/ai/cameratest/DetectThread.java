package com.zjgsu.ai.cameratest;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import comm.zjgsu.face.npddetect;
import comm.zjgsu.face.vectori;

/**
 * Created by Double on 09/11/2017.
 */

public class DetectThread extends Thread {

    private final String TAG = "MSG_Camera";

    private byte[] imgBytes;
    private int mWidth;
    private int mHeight;
    private npddetect mNpdDetect;
    public static boolean execute = false;
    private ViewController mController;

    public DetectThread(ViewController controller, byte[] bytes, int width, int height, npddetect npd) {
        mController = controller;
        imgBytes = bytes;
        mWidth = width;
        mHeight = height;
        mNpdDetect = npd;
    }

    @Override
    public void run() {
        super.run();
        mNpdDetect.detect(imgBytes, mWidth, mHeight);
        vectori x = mNpdDetect.getXs();
        vectori y = mNpdDetect.getYs();
        vectori s = mNpdDetect.getSs();
        int[] result = new int[(int) (3 * x.size())];
        for (int i = 0; i < x.size(); ++i) {
            result[i] = x.get(i);
            result[i + 1] = y.get(i);
            result[i + 2] = s.get(i);
        }
        mController.sendFaces(result);
        execute = false;
    }

    public static void detect(byte[] bytes, int width, int height, ViewController controller, npddetect npd) {
        if (!execute) {
            execute = true;
            new DetectThread(controller, bytes, width, height, npd).start();
        }
    }

//    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}