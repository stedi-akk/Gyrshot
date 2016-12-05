package com.stedi.gyrshot;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.stedi.gyrshot.camera.CameraActivity;
import com.stedi.gyrshot.layers.TestLayer;
import com.stedi.gyrshot.layers.TestLayer2;

public class MainActivity extends CameraActivity implements SensorEventListener {
    private LayersView layersView;
    private ViewGroup cameraPreviewContainer;

    private SensorManager sensorManager;
    private Sensor gyroSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        layersView = (LayersView) findViewById(R.id.layers_view);
        cameraPreviewContainer = (ViewGroup) findViewById(R.id.camera_preview_container);
        layersView.addLayer(new TestLayer());
        layersView.addLayer(new TestLayer2());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavbar();
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected ViewGroup getPreviewContainer() {
        return cameraPreviewContainer;
    }

    @Override
    public void onCameraInit(boolean result) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float gyroX = (float) Math.toDegrees(event.values[0]);
        float gyroY = (float) -Math.toDegrees(event.values[1]);
        layersView.update(gyroX, gyroY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void hideNavbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
