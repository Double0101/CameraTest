package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraSurface";

    private CameraPreview mCameraPreview;

    private SurfaceHolder mHolder;

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCameraPreview = new CameraPreview(640, 480);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void setController(ViewController controller) {
        mCameraPreview.setController(controller);
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
