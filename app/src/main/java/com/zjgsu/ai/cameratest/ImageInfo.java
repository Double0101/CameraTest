package com.zjgsu.ai.cameratest;

/**
 * Created by Double on 07/12/2017.
 */

public class ImageInfo {

    private int mWidth;
    private int mHeight;
    private byte[] data;
    private int mOrientation;

    public ImageInfo(int width, int height) {
        mWidth = width;
        mHeight = height;
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
}
