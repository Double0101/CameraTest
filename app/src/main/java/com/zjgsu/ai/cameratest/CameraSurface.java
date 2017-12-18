package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "CameraSurface";

    private CameraPreview mCameraPreview;

    private SurfaceHolder mHolder;
    private ViewController mController;

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCameraPreview = new CameraPreview(640, 480);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void setController(ViewController controller) {
        mController = controller;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        cameraPreview(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        cameraPreview(surfaceHolder);
        mCameraPreview.setPreviewCallback();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if (bytes == null) return;

        mController.sendImage(bytes);
    }

    public void openCamera() {
        mCameraPreview.openCamera();
    }

    public void cameraPreview(SurfaceHolder holder) {
        mCameraPreview.cameraPreview(holder);
    }

    public void stopPreview() {
        mCameraPreview.stopPreview();
    }

    public void cameraRelease() {
        mCameraPreview.cameraRelease();
    }

    public void changeCamera() {
        mCameraPreview.changeCamera(mHolder);
    }

}
