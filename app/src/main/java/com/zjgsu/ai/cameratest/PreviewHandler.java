package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import comm.zjgsu.face.npddetect;

/**
 * Created by Double on 09/11/2017.
 */

public class PreviewHandler extends Handler {
    private final String TAG = "MSG_Camera";

    public static final int UPDATE_DETECT = 0;
    public static final int UPDATE_VIEW = 1;

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

}
