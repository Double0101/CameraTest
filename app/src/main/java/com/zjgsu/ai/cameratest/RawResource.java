package com.zjgsu.ai.cameratest;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Double on 13/10/2017.
 */

public class RawResource {
    private int mId;
    private Context mContext;

    public RawResource(Context context, int id) {
        mContext = context;
        mId = id;
    }

    @Override
    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writeTo(out);
        } catch (Exception e) {
            return null;
        }
        return out.toString();
    }

    public File save(String fileName, boolean overWrite) {
        if (!overWrite && mContext.getFileStreamPath(fileName).exists()) {
            return mContext.getFileStreamPath(fileName);
        }

        try {
            OutputStream out = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            writeTo(out);
            out.close();
        } catch (IOException e) {
            return null;
        }
        return mContext.getFileStreamPath(fileName);
    }

    private void writeTo(OutputStream out) throws IOException {
        InputStream in = mContext.getResources().openRawResource(mId);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
    }
}
