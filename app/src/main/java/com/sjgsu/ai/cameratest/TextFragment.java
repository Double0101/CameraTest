package com.sjgsu.ai.cameratest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by double on 17-8-23.
 */

public class TextFragment extends Fragment {

    static {
        System.loadLibrary("native-lib");
    }

    private TextView mTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_layout, container, false);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        mTextView.setText(getString());
        return view;
    }

    public native String getString();
}
