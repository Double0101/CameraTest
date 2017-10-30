package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceHolder mHolder;
    private CameraFragment mParentFragment;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private String modelPath;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void setParentFragment(CameraFragment fragment) {
        mParentFragment = fragment;
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
        cameraPreview();
        mCamera.setPreviewCallback(this);
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
            mParameters = mCamera.getParameters();
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mParameters.setPreviewFormat(ImageFormat.NV21);
            mCamera.setParameters(mParameters);

        } catch (Exception e) {
            e.printStackTrace();
            cameraRelease();
            return false;
        }

        return true;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

        // test
        if (bytes == null) return;
        Log.i("JNIMSG", "byte length " + bytes.length);
        Log.i("JNIMSG", "PreviewSize width & height " + mParameters.getPreviewSize().width + " " + mParameters.getPreviewSize().height);
        Log.i("JNIMSG", "Camera size " + camera.getParameters().getPreviewSize().width + " " + camera.getParameters().getPreviewSize().height);

        mParentFragment.drawFaces(testDetect(bytes,
                camera.getParameters().getPreviewSize().width,
                camera.getParameters().getPreviewSize().height,
                modelPath));
    }

    private void cameraPreview() {
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            cameraRelease();
            e.printStackTrace();
        }
    }

    private void cameraRelease() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}
