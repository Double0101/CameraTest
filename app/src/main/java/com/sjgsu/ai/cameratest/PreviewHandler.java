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

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    private FaceView mFaceView;
    private Camera.PreviewCallback mCamera;
    private String modelPath;

    public PreviewHandler(Context context, FaceView faceView, Camera.PreviewCallback camera) {
        mFaceView = faceView;
        mCamera = camera;
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_DETECT:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = obtainMessage();
                        message.what = PreviewHandler.UPDATE_VIEW;
                        message.obj = testDetect((byte[]) msg.obj, 640, 480, modelPath);
                        message.sendToTarget();
                    }
                }).start();
                break;
            case UPDATE_VIEW:
                mFaceView.setFaces((int[]) msg.obj);
            default:
                break;
        }
        super.handleMessage(msg);
    }

    public native int[] testDetect(byte[] bytes, int width, int height, String result);

}
