package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
    private boolean execute = false;
    private Handler mHandler;

    public DetectThread(Context context, Handler handler) {
        mHandler = handler;
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
    }

    @Override
    public void run() {
        super.run();
        Message msg = mHandler.obtainMessage();
        msg.what = PreviewHandler.UPDATE_VIEW;
        msg.obj = testDetect(imgBytes, mWidth, mHeight, modelPath);
        msg.sendToTarget();
        execute = false;
    }

    public void detect(byte[] bytes, int width, int height) {
        if (!execute) {
            execute = true;
            imgBytes = bytes;
            mWidth = width;
            mHeight = height;
            this.start();
        }
    }

    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}
