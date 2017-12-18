package com.zjgsu.ai.cameratest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Double on 25/09/2017.
 */

public class CameraFragment extends Fragment implements View.OnClickListener{
    
    private static final String TAG = "Double_Rotation";

    private FaceView mFaceView;
    private CameraSurface mCameraSurface;
    private ImageButton mBtnChangeCamera;

    private ViewController mController;
    private RotationSensor mSensor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_layout, container, false);

        mCameraSurface = (CameraSurface) view.findViewById(R.id.camera_surface);
        mFaceView = (FaceView) view.findViewById(R.id.faces_view);
        mBtnChangeCamera = (ImageButton) view.findViewById(R.id.img_btn);

        mSensor = new RotationSensor(getContext());
        mController = new PreviewHandler(getContext(), mFaceView, mSensor);

        mCameraSurface.setController(mController);
        mBtnChangeCamera.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_btn) {
            mCameraSurface.changeCamera();
        }
    }
}
