package com.zjgsu.ai.cameratest;

import android.hardware.Camera;

/**
 * Created by Double on 07/12/2017.
 */

public class ImageInfo {

    private int mWidth;
    private int mHeight;
    private byte[] data;
    private int mOrientation;

    public ImageInfo(int width, int height) {
        mHeight = height;
        mWidth = width;
    }

    public ImageInfo getDetectInfo(byte[] bytes, int orientation) {
        data = bytes;
        mOrientation = orientation;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public static ImageInfo getIntance(int width, int height) {
        return new ImageInfo(width, height);
    }
}
