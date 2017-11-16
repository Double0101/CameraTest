package com.zjgsu.ai.cameratest;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraFragment extends Fragment {

    private FaceView mFaceView;
    private CameraSurface mCameraSurface;

    private ViewController mController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        mCameraSurface = (CameraSurface) view.findViewById(R.id.camera_surface);
        mFaceView = (FaceView) view.findViewById(R.id.faces_view);

        mController = new PreviewHandler(getContext(), mFaceView);
        mCameraSurface.setController(mController);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraSurface.openCamera();
        mController.loadNpd();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraSurface.cameraRelease();
        mController.releaseNpd();
    }
}
