package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by Double on 09/11/2017.
 */

public class PreviewHandler extends Handler {

    public static final int UPDATE_DETECT = 0;
    public static final int UPDATE_VIEW = 1;

    private FaceView mFaceView;
    private String modelPath;

    public PreviewHandler(Context context, FaceView faceView) {
        mFaceView = faceView;
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_DETECT:
                Log.i("MSGDOUBLE", "updatedetect");
                DetectThread.detect((byte[]) msg.obj, 640, 480, this, modelPath);
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
