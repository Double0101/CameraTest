package com.zjgsu.ai.cameratest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Double on 14/12/2017.
 */

public class RotationSensor implements SensorEventListener{
    private String TAG = "RotationSensor";

    private static int DEFAULT_ROTATION = 0;
    private static int ROTATION_90 = 1;
    private static int ROTATION_180 = 2;
    private static int ROTATION_270 = 3;

    private Context mContext;
    private SensorManager mManager;
    private Sensor mGravSensor;

    private int mRotation = DEFAULT_ROTATION;

    public RotationSensor(Context context) {
        mContext = context;
        mManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        getRotationSensor();
    }

    private void getRotationSensor() {
        if (mManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
            mGravSensor = mManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        if (mGravSensor == null)
            Toast.makeText(mContext, "Your Device Cannot get RotationInfo", Toast.LENGTH_SHORT).show();
    }

    public void registerSensor() {
        mManager.registerListener(this, mGravSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (Math.abs(sensorEvent.values[2]) > 8.67f || sensorEvent.values[0] > 8.8f) {
            mRotation = DEFAULT_ROTATION;
            return;
        }
        if (sensorEvent.values[0] < -8.6f) {
            mRotation = ROTATION_180;
            return;
        }
        if (sensorEvent.values[1] > 8.6f) {
            mRotation = ROTATION_90;
            return;
        }
        if (sensorEvent.values[1] < -8.6f) {
            mRotation = ROTATION_270;
            return;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void unregisterSensor() {
        mManager.unregisterListener(this, mGravSensor);
    }

    public int getRotation() {
        return mRotation;
    }
}
