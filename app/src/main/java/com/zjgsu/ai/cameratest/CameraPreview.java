package com.zjgsu.ai.cameratest;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Double on 18/12/2017.
 */

public class CameraPreview implements Camera.PreviewCallback {

    private Camera mCamera;
    private ImageInfo mInfo;

    private int cCameraType;

    public CameraPreview(int width, int height) {
        mInfo = new ImageInfo(width, height);
    }

    public boolean openCamera() {
        return openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    private void setParameters(int cameraType) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            parameters.setPreviewSize(mInfo.getWidth(), mInfo.getHeight());
            parameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(parameters);
            cCameraType = cameraType;
        } catch (Exception e) {
            e.printStackTrace();
            cameraRelease();
        }
    }

    public boolean openCamera(int cameraType) {
        mCamera = Camera.open(cameraType);
        if (mCamera == null) {
            return false;
        }
        setParameters(cameraType);

        return true;
    }

    public void changeCamera(SurfaceHolder holder) {
        if (mCamera == null) {
            return;
        }
        stopPreview();
        cameraRelease();
        if (cCameraType == Camera.CameraInfo.CAMERA_FACING_BACK) {
            openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }
        else if (cCameraType == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        cameraPreview(holder);
    }

    public void cameraPreview(SurfaceHolder holder) {
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            cameraRelease();
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
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

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }
}
