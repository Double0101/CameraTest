package com.sjgsu.ai.cameratest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

/**
 * Created by double on 17-8-23.
 */

public class TextFragment extends Fragment {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    private TextView mTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_layout, container, false);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        mTextView.setText(getString());

        if (!OpenCVLoader.initDebug()) {
            mTextView.setText(mTextView.getText() + "\n OpenCVLoader initDebug() not work");
        } else {
            mTextView.setText(mTextView.getText() + "\n OpenCVLoader initDebug() working");
            mTextView.setText(mTextView.getText() + "\n" + testOpenCV(0l, 0l));
        }

        return view;
    }

    public native String getString();
    public native String testOpenCV(long matAddrGr, long matAddrRgba);
}
