package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Double on 18/10/2017.
 */

public class FaceView extends SurfaceView {

    private int[] mFaces;
    private Paint mPaint;
    private SurfaceHolder mHolder;
    private double scaleX, scaleY;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        setZOrderOnTop(true);
        mHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(7f);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scaleX = dm.widthPixels / 640;
        scaleY = dm.heightPixels / 480;
    }

    public void setFaces(int[] faces) {
        this.mFaces = faces;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFaces != null) {
            Log.i("MSGDOUBLE", "onDraw");
            for (int i = 0; i < mFaces.length; i = i + 3) {
                canvas.drawRect((int) (mFaces[i] * scaleX),
                        (int) (mFaces[i + 1] * scaleY),
                        (int) ((mFaces[i] + mFaces[i + 2]) * scaleX),
                        (int) ((mFaces[i + 1] + mFaces[i + 2]) * scaleY),
                        mPaint);
            }
        }
        super.onDraw(canvas);
    }
}
