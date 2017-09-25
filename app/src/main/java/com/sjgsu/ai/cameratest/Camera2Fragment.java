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

public class Camera2Fragment extends Fragment {

    private Camera2Surface mCamera2Surface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera2_layout, container, false);
        mCamera2Surface = (Camera2Surface) view.findViewById(R.id.camera2_surface);
        return view;
    }
}
