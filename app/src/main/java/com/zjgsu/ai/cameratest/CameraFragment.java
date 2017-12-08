package com.zjgsu.ai.cameratest;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraFragment extends Fragment {
    
    private static final String TAG = "Double_Rotation";

    private FaceView mFaceView;
    private CameraSurface mCameraSurface;

    private ViewController mController;
    private OrientationListener mOrientationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        mCameraSurface = (CameraSurface) view.findViewById(R.id.camera_surface);
        mFaceView = (FaceView) view.findViewById(R.id.faces_view);

        mOrientationListener = new OrientationListener(getContext());
        mController = new PreviewHandler(getContext(), mFaceView, mOrientationListener);

        mCameraSurface.setController(mController);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraSurface.openCamera();
        mController.loadControl();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraSurface.cameraRelease();
        mController.releaseControl();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOrientationListener.disable();
    }
}
