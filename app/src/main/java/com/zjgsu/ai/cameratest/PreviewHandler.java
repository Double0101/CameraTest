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

    public static final int UPDATE_DETECT = 0;
    public static final int UPDATE_VIEW = 1;

    private FaceView mFaceView;

    private npddetect mNpdDetect;

    public PreviewHandler(Context context, FaceView faceView) {
        mFaceView = faceView;
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        String modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
        mNpdDetect = new npddetect();
        mNpdDetect.load(modelPath);
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_DETECT:
                Log.i("MSGDOUBLE", "updatedetect");
                DetectThread.detect((byte[]) msg.obj, 640, 480, this, mNpdDetect);
                break;
            case UPDATE_VIEW:
                Log.i("MSGDOUBLE", "updateview");
                mFaceView.setFaces((int[]) msg.obj);
            default:
                break;
        }
        super.handleMessage(msg);
    }

}
