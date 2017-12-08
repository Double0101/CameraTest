package com.zjgsu.ai.cameratest;

import android.util.Log;

import com.zjgsu.face.npddetect;
import com.zjgsu.face.vectorf;
import com.zjgsu.face.vectori;

/**
 * Created by Double on 09/11/2017.
 */

public class DetectThread extends Thread {

    private final String TAG = "MSG_Camera";

    private ImageInfo mImageInfo;
    private npddetect mNpdDetect;
    public static boolean execute = false;
    private ViewController mController;

    public DetectThread(ViewController controller, ImageInfo info, npddetect npd) {
        mController = controller;
        mImageInfo = info;
        mNpdDetect = npd;
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "run Orientation: " + mImageInfo.getOrientation());
        int n = mNpdDetect.detect(mImageInfo.getData(), mImageInfo.getWidth(), mImageInfo.getHeight());
        vectori x = mNpdDetect.getXs();
        vectori y = mNpdDetect.getYs();
        vectori s = mNpdDetect.getSs();
        vectorf f = mNpdDetect.getScores();
        int[] result = new int[(int) (3 * x.size())];
        Log.i(TAG, "run: " + x.size() + "  " + n);
        for (int i = 0; i < x.size(); ++i) {
            if (f.get(i) < 10) {
                result[i] = result[i + 1] = result[i + 2] = 0;
                continue;
            }
            result[i] = x.get(i);
            result[i + 1] = y.get(i);
            result[i + 2] = s.get(i);
        }
        mController.sendFaces(result);
        execute = false;
    }

    public static void detect(ImageInfo info, ViewController controller, npddetect npd) {
        if (!execute) {
            execute = true;
            new DetectThread(controller, info, npd).start();
        }
    }
}
