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
    private SurfaceHolder mHolder;
    private ViewController mController;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private int curCameraType;

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        curCameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void setController(ViewController controller) {
        mController = controller;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        cameraPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        cameraPreview();
        mCamera.setPreviewCallback(this);
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

    public boolean openCamera() {
        return openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public boolean openCamera(int cameraType) {
        mCamera = Camera.open(cameraType);
        if (mCamera == null) {
            return false;
        }
        try {
            curCameraType = cameraType;
            mParameters = mCamera.getParameters();
            List<String> focusModes = mParameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            mParameters.setPreviewSize(640, 480);
            mParameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
            cameraRelease();
            return false;
        }

        return true;
    }

    public void cameraPreview() {
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            cameraRelease();
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
    }

    public void cameraRelease() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public void changeCamera() {
        Log.i(TAG, "CAMERA BACK " + Camera.CameraInfo.CAMERA_FACING_BACK);
        Log.i(TAG, "CAMERA FRONT " + Camera.CameraInfo.CAMERA_FACING_FRONT);
        if (mCamera != null) {
            stopPreview();
            cameraRelease();
            if (curCameraType == Camera.CameraInfo.CAMERA_FACING_BACK) {
                openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            cameraPreview();
        }
    }

}
