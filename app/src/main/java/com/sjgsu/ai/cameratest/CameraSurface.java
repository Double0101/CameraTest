package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!openCamera()) {
            cameraPreview();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.stopPreview();
        if (mCamera != null) {
            mParameters = mCamera.getParameters();
            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(mParameters);
        }
        cameraPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraRelease();
    }

    private boolean openCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        try {
            mCamera = Camera.open();
            setCameraCallback();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    private void setCameraCallback() {
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                bytes = testDetect(bytes,
                        camera.getParameters().getPreviewSize().width,
                        camera.getParameters().getPreviewSize().height);
            }
        });
    }

    private void cameraPreview() {
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraRelease() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public native byte[] testDetect(byte[] bytes, int width, int height);
}
