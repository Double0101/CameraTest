package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * Created by Double on 07/12/2017.
 */

public class OrientationListener extends OrientationEventListener {

    private static final String TAG = "OrientationListener";

    private int mOrientation = 3;

    public OrientationListener(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int i) {
        Log.i(TAG, "onOrientationChanged: " + mOrientation);
        mOrientation = ((i + 45) / 90) % 4;
    }

    public int getCurrentOrientation() {
        return mOrientation;
    }
}
