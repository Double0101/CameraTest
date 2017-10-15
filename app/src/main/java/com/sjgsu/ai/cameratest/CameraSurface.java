package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.core.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private int[] faces;
    private String modelPath;
    private Paint mPaint;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setZOrderOnTop(true);
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(7f);
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
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPreviewFormat(ImageFormat.YV12);
            mCamera.setParameters(parameters);

        } catch (Exception e) {
            e.printStackTrace();
            cameraRelease();
            return false;
        }

        return true;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Log.i("fdsdfs", "onPreviewFrame");
        faces = testDetect(bytes,
                camera.getParameters().getPreviewSize().width,
                camera.getParameters().getPreviewSize().height,
                modelPath);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faces != null) {
            for (int i = 0; i < faces.length; i += 4) {
                canvas.drawRect(faces[i], faces[i + 1], faces[i + 2], faces[i + 3], mPaint);
            }
        }
    }
}
