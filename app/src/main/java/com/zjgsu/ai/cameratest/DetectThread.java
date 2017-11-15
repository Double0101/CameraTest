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
    private byte[] imgBytes;
    private int mWidth;
    private int mHeight;
    private npddetect mNpdDetect;
    public static boolean execute = false;
    private Handler mHandler;

    public DetectThread(Handler handler, byte[] bytes, int width, int height, npddetect npd) {
        mHandler = handler;
        imgBytes = bytes;
        mWidth = width;
        mHeight = height;
        mNpdDetect = npd;
    }

    @Override
    public void run() {
        super.run();
        Log.i("MSGDOUBLE", "run");
        Message msg = mHandler.obtainMessage();
        msg.what = PreviewHandler.UPDATE_VIEW;
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
        Log.i("MSGDOUBLENNNN", x.size()+"");
        msg.obj = result;
        msg.sendToTarget();
        execute = false;
    }

    public static void detect(byte[] bytes, int width, int height, Handler handler, npddetect npd) {
        if (!execute) {
            execute = true;
            new DetectThread(handler, bytes, width, height, npd).start();
        }
    }

//    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}
