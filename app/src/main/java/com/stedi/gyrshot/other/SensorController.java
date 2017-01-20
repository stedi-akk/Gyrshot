package com.stedi.gyrshot.other;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.stedi.gyrshot.constants.AppConfig;

public class SensorController implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gyroSensor, rotationSensor;

    private float[] rotationMatrix;
    private float[] rotationMatrixTemp;
    private float[] rotationMatrixValues;

    private float lastGyroX, lastGyroY, lastRotationZ;

    private SensorListener listener;

    public interface SensorListener {
        void fromGyroscope(float xDegree, float yDegree);

        void fromRotationVector(float zDegree);
    }

    public SensorController(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (AppConfig.ALLOW_ROTATION_SENSOR) {
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            rotationMatrix = new float[16];
            rotationMatrixTemp = new float[16];
            rotationMatrixValues = new float[16];
        }
    }

    public void startListening(SensorListener listener) {
        this.listener = listener;
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopListening() {
        sensorManager.unregisterListener(this);
        this.listener = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (listener == null)
            return;
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (Math.abs(event.values[0] - lastGyroX) > AppConfig.GYROSCOPE_SENSOR_ACCURACY
                    || Math.abs(event.values[1] - lastGyroY) > AppConfig.GYROSCOPE_SENSOR_ACCURACY) {

                lastGyroX = event.values[0];
                lastGyroY = event.values[1];

                listener.fromGyroscope((float) Math.toDegrees(lastGyroX),
                        (float) -Math.toDegrees(lastGyroY));
            }
        } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            if (Math.abs(event.values[2] - lastRotationZ) > AppConfig.ROTATION_SENSOR_ACCURACY) {

                lastRotationZ = event.values[2];

                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X, SensorManager.AXIS_Z,
                        rotationMatrixTemp);
                SensorManager.getOrientation(rotationMatrixTemp, rotationMatrixValues);

                float rotationZ = (float) -Math.toDegrees(rotationMatrixValues[2]) - 90;
                listener.fromRotationVector(rotationZ);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
