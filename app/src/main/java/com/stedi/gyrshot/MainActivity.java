package com.stedi.gyrshot;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stedi.gyrshot.camera.CameraActivity;
import com.stedi.gyrshot.constants.AppConfig;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.TargetsLayer;
import com.stedi.gyrshot.layers.ZoneLayer;
import com.stedi.gyrshot.layers.menus.StartMenuLayer;
import com.stedi.gyrshot.layers.targets.DecreasesTarget;
import com.stedi.gyrshot.other.Mode;
import com.stedi.gyrshot.overlay.OverlayView;

public class MainActivity extends CameraActivity implements SensorEventListener, OverlayView.Listener {
    private ViewGroup cameraPreviewContainer;
    private LayersView layersView;
    private OverlayView overlayView;

    private SensorManager sensorManager;
    private Sensor gyroSensor;

    private float lastGyroX, lastGyroY;

    private Layer currentLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        cameraPreviewContainer = (ViewGroup) findViewById(R.id.main_activity_camera_preview_container);
        layersView = (LayersView) findViewById(R.id.main_activity_layers_view);
        overlayView = (OverlayView) findViewById(R.id.main_activity_overlay_view);
        initGyroscope();
        initLayersAndOverlay();
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
        if (Math.abs(gyroX - lastGyroX) > AppConfig.GYROSCOPE_ACCURACY
                || Math.abs(gyroY - lastGyroY) > AppConfig.GYROSCOPE_ACCURACY) {
            lastGyroX = gyroX;
            lastGyroY = gyroY;
            layersView.updateFromGyroscope(lastGyroX, lastGyroY);
        }
    }

    @Override
    public void onShot() {
        ShotCallback callback = layersView.onShot();
        if (callback != null) {
            if (callback instanceof DecreasesTarget.OnShot) {
                Toast.makeText(this, "Gotcha !", Toast.LENGTH_SHORT).show();
                return;
            }
            if (callback instanceof StartMenuLayer.OnShot) {
                StartMenuLayer.OnShot onShot = (StartMenuLayer.OnShot) callback;
                switch (onShot.type) {
                    case START_GAME:
                        Mode.setCurrent(Mode.GAME);
                        currentLayer = new TargetsLayer();
                        layersView.setMode(Mode.getCurrent());
                        overlayView.setMode(Mode.getCurrent());
                        layersView.addLayer(currentLayer, true);
                        break;
                    case EXIT:
                        finish();
                        break;
                }
                return;
            }
        }
    }

    @Override
    public void onCameraClick() {

    }

    @Override
    public void onSoundsClick() {

    }

    private void initLayersAndOverlay() {
        if (Mode.getCurrent() == null) { // first launch
            Mode.setCurrent(Mode.MENU);
            layersView.addLayer(new ZoneLayer());
            currentLayer = new StartMenuLayer();
            layersView.addLayer(currentLayer, true);
        }
        layersView.setMode(Mode.getCurrent());
        overlayView.setMode(Mode.getCurrent());
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
