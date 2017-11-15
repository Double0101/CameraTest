package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import comm.zjgsu.face.npddetect;

/**
 * Created by Double on 09/11/2017.
 */

public class PreviewHandler extends Handler implements ViewController{
    private final String TAG = "MSG_Camera";

    private FaceView mFaceView;

    private npddetect mNpdDetect;

    public PreviewHandler(Context context, FaceView faceView) {
        mFaceView = faceView;
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        String modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
        Log.i(TAG, "PreviewHandler: " + modelPath);
        mNpdDetect = new npddetect();
        mNpdDetect.load(modelPath);
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_DETECT:
                DetectThread.detect((byte[]) msg.obj, 640, 480, this, mNpdDetect);
                break;
            case UPDATE_VIEW:
                mFaceView.setFaces((int[]) msg.obj);
            default:
                break;
        }
        super.handleMessage(msg);
    }

    @Override
    public void sendImage(byte[] bytes) {
        Message msg = obtainMessage();
        msg.what = UPDATE_DETECT;
        msg.obj = bytes;
        msg.sendToTarget();
    }

    @Override
    public void sendFaces(int[] faces) {
        Message msg = obtainMessage();
        msg.what = UPDATE_VIEW;
        msg.obj = faces;
        msg.sendToTarget();
    }
}
