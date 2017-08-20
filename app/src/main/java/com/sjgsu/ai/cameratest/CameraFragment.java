package com.sjgsu.ai.cameratest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Double on 19/08/2017.
 */

public class CameraFragment extends Fragment {

    private CameraSurface mCameraSurface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout, container, false);
        mCameraSurface = (CameraSurface) view.findViewById(R.id.camera_surface);
        return view;
    }
}
