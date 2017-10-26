package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Double on 18/10/2017.
 */

public class FaceView extends SurfaceView {

    private int[] mFaces;
    private Paint mPaint;

    private Bitmap mBitmap;

    private SurfaceHolder mHolder;

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
    }

    public void setFaces(int[] faces) {
        this.mFaces = faces;
        invalidate();
    }

    public void test(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFaces != null) {
            for (int i = 0; i < mFaces.length; i = i + 4) {
                canvas.drawRect(mFaces[i], mFaces[i + 1], mFaces[i + 2], mFaces[i + 3], mPaint);
            }
        }
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        super.onDraw(canvas);
    }
}
