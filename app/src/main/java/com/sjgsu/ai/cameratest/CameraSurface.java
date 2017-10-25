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

//    private String imgPath;
    private int width, height;
    private byte[] testByte;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public CameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        RawResource mRawResource = new RawResource(context, R.raw.newmodel);
        modelPath = mRawResource.save("model_one.bin", false).getAbsolutePath();

        // test
//        RawResource rawResource = new RawResource(context, R.raw.img_test_jni);
//        imgPath = rawResource.save("img_test.jpg", false).getAbsolutePath();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
//        width = options.outWidth;
//        height = options.outHeight;

        Drawable drawable = getResources().getDrawable(R.drawable.img_test_jni);
        Bitmap bitmap = convertGrayImg(((BitmapDrawable) drawable).getBitmap());
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        Log.i("JNIMSG", width + " " + height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        testByte = out.toByteArray();
        Log.i("JNIMSG", "byte[] length " + testByte.length);

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

        // test

        testDetect(testByte,
                width,
                height,
                modelPath);

//        mParentFragment.drawFaces(testDetect(bytes,
//                camera.getParameters().getPreviewSize().width,
//                camera.getParameters().getPreviewSize().height,
//                modelPath));
    }

    private Bitmap convertGrayImg(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayscale;
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

//    public native int[] testDetect(String img, int width, int height, String model);
    public native int[] testDetect(byte[] bytes, int width, int height, String result);
}
