package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Double on 09/11/2017.
 */

public class DetectThread extends Thread {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    private byte[] imgBytes;
    private int mWidth;
    private int mHeight;
    private String modelPath;
    public static boolean execute = false;
    private Handler mHandler;

    public DetectThread(Handler handler, byte[] bytes, int width, int height, String path) {
        mHandler = handler;
        imgBytes = bytes;
        mWidth = width;
        mHeight = height;
        modelPath = path;
    }

    @Override
    public void run() {
        super.run();
        Log.i("MSGDOUBLE", "run");
        Message msg = mHandler.obtainMessage();
        msg.what = PreviewHandler.UPDATE_VIEW;
        msg.obj = testDetect(imgBytes, mWidth, mHeight, modelPath);
        msg.sendToTarget();
        execute = false;
    }

    public static void detect(byte[] bytes, int width, int height, Handler handler, String path) {
        if (!execute) {
            execute = true;
            new DetectThread(handler, bytes, width, height, path).start();
        }
    }

    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}
