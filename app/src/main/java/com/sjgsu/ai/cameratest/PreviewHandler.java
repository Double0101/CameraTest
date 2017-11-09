package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;

/**
 * Created by Double on 09/11/2017.
 */

public class PreviewHandler extends Handler {

    public static final int UPDATE_DETECT = 0;
    public static final int UPDATE_VIEW = 1;

    private FaceView mFaceView;
    private DetectThread mThread;

    public PreviewHandler(Context context, FaceView faceView) {
        mFaceView = faceView;
        mThread = new DetectThread(context, this);
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_DETECT:
                mThread.detect((byte[]) msg.obj, 640, 480);
                break;
            case UPDATE_VIEW:
                mFaceView.setFaces((int[]) msg.obj);
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
