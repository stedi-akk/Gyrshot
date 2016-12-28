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
import com.stedi.gyrshot.config.Config;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.ZoneLayer;
import com.stedi.gyrshot.layers.menus.StartMenuLayer;
import com.stedi.gyrshot.other.Mode;
import com.stedi.gyrshot.overlay.OverlayView;

public class MainActivity extends CameraActivity implements SensorEventListener, OverlayView.Listener {
    private ViewGroup cameraPreviewContainer;
    private LayersView layersView;
    private OverlayView overlayView;

    private SensorManager sensorManager;
    private Sensor gyroSensor;

    private float lastGyroX, lastGyroY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cameraPreviewContainer = (ViewGroup) findViewById(R.id.main_activity_camera_preview_container);
        layersView = (LayersView) findViewById(R.id.main_activity_layers_view);
        overlayView = (OverlayView) findViewById(R.id.main_activity_overlay_view);
        initLayers();
        initOverlay();
        initGyroscope();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
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
    public void onCameraOpen(boolean result) {
        layersView.setTransparent(result);
    }

    @Override
    public void onCameraRelease() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float gyroX = (float) Math.toDegrees(event.values[0]);
        float gyroY = (float) -Math.toDegrees(event.values[1]);
        if (Math.abs(gyroX - lastGyroX) > Config.GYROSCOPE_ACCURACY
                || Math.abs(gyroY - lastGyroY) > Config.GYROSCOPE_ACCURACY) {
            lastGyroX = gyroX;
            lastGyroY = gyroY;
            layersView.updateFromGyroscope(lastGyroX, lastGyroY);
        }
    }

    @Override
    public void onShot() {
        layersView.onShot();
    }

    @Override
    public void onCameraClick() {

    }

    @Override
    public void onSoundsClick() {

    }

    private void initLayers() {
        layersView.setMode(Mode.MENU);
        layersView.addLayer(new ZoneLayer());
//        layersView.addLayer(new TargetsLayer());
        layersView.addLayer(new StartMenuLayer());
    }

    private void initOverlay() {
        overlayView.setMode(Mode.MENU);
        overlayView.setListener(this);
    }

    private void initGyroscope() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
