package com.sjgsu.ai.cameratest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraFragment extends Fragment {

    private FaceView mFaceView;
    private CameraSurface mCameraSurface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        mCameraSurface = (CameraSurface) view.findViewById(R.id.camera_surface);
        mCameraSurface.setParentFragment(this);
        mFaceView = (FaceView) view.findViewById(R.id.faces_view);
        return view;
    }

    public void drawFaces(int[] faces) {
        mFaceView.setFaces(faces);
    }
}
