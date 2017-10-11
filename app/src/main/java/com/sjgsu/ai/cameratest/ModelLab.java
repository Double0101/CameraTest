package com.sjgsu.ai.cameratest;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Double on 10/10/2017.
 */

public class ModelLab {
    private static ModelLab sModelLab;
    private Context mAppContext;
    private String modelPath;
    public static String getModelPath(Context c) {
        if (sModelLab == null) {
            sModelLab = new ModelLab(c.getApplicationContext());
        }
        return sModelLab.modelPath;
    }
    private ModelLab(Context appContext) {
        mAppContext = appContext;

        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        mAppContext.getResources().openRawResource(R.raw.newmodel)));
            File output = new File("model.bin");
            PrintWriter writer = new PrintWriter(output);
            String line = "";
            while ((line = in.readLine()) != null) {
                writer.write(line);
            }
            writer.close();
            modelPath = output.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
