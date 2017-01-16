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
import com.stedi.gyrshot.layers.GameLayer;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.ZoneLayer;
import com.stedi.gyrshot.layers.menus.PickGameMenuLayer;
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

    private static Mode currentMode;

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
    public void onBackPressed() {
        if (layersView.getBackStack().size() <= 1)
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        App.onResume();
        layersView.onResume();
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        layersView.onPause();
        App.onPause();
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
        layersView.setTransparent(false);
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
                        changeModeTo(Mode.MENU);
                        layersView.addLayer(new PickGameMenuLayer(), true);
                        invalidateOverlayBackButton();
                        break;
                    case EXIT:
                        finish();
                        break;
                }
                return;
            }
            if (callback instanceof PickGameMenuLayer.OnShot) {
                PickGameMenuLayer.OnShot onShot = (PickGameMenuLayer.OnShot) callback;
                changeModeTo(Mode.GAME);
                layersView.addLayer(new GameLayer(onShot.type), true);
                invalidateOverlayBackButton();
            }
        }
    }

    @Override
    public void onBackClick() {
        boolean result = layersView.popBackStack();
        invalidateOverlayBackButton();
        if (result) {
            Layer layer = layersView.getBackStack().peek();
            if (layer instanceof GameLayer)
                changeModeTo(Mode.GAME);
            else
                changeModeTo(Mode.MENU);
        }
    }

    @Override
    public void onCameraClick() {
        if (isCameraOpen())
            disableCamera();
        else
            allowCamera();
    }

    @Override
    public void onSoundsClick() {

    }

    private void invalidateOverlayBackButton() {
        boolean visible = layersView.getBackStack().size() > 1;
        overlayView.setBackButtonVisible(visible);
    }

    private void initLayersAndOverlay() {
        Mode initMode = currentMode;
        if (initMode == null) { // first launch
            initMode = Mode.MENU;
            layersView.addLayer(new ZoneLayer());
            layersView.addLayer(new StartMenuLayer(), true);
        }
        changeModeTo(initMode);
        overlayView.setListener(this);
        invalidateOverlayBackButton();
    }

    private void changeModeTo(Mode mode) {
        currentMode = mode;
        layersView.setMode(currentMode);
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
