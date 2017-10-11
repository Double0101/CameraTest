package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
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

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private int[] faces;
    private String mResultString;
    private Paint mPaint;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setZOrderOnTop(true);
        mResultString = ModelLab.getModelPath(context);
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
                faces = testDetect(bytes,
                        camera.getParameters().getPreviewSize().width,
                        camera.getParameters().getPreviewSize().height,
                        mResultString);
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
