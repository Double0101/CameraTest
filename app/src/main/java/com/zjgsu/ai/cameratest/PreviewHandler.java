package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.zjgsu.face.npddetect;

/**
 * Created by Double on 09/11/2017.
 */

public class PreviewHandler extends Handler implements ViewController{
    private final String TAG = "MSG_Camera";

    private FaceView mFaceView;

    private Context mContext;

    private npddetect mNpdDetect;

    public PreviewHandler(Context context, FaceView faceView) {
        mFaceView = faceView;
        mContext = context;
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case UPDATE_VIEW:
                mFaceView.setFaces((int[]) msg.obj);
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }

    @Override
    public void sendImage(byte[] bytes) {
        DetectThread.detect(bytes, 640, 480, this, mNpdDetect);
    }

    @Override
    public void sendFaces(int[] faces) {
        Message msg = obtainMessage();
        msg.what = UPDATE_VIEW;
        msg.obj = faces;
        msg.sendToTarget();
    }

    @Override
    public void releaseNpd() {
        mNpdDetect.delete();
    }

    @Override
    public void loadNpd() {
        RawResource rawResource = new RawResource(mContext, R.raw.newmodel);
        mNpdDetect = new npddetect();
        mNpdDetect.load(rawResource.save("model_one.bin", false).getAbsolutePath());
    }
}
