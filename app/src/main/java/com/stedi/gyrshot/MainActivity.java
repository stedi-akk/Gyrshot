package com.stedi.gyrshot;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stedi.gyrshot.camera.CameraActivity;
import com.stedi.gyrshot.constants.CoreConfig;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.ShotPointerLayer;
import com.stedi.gyrshot.layers.ZoneLayer;
import com.stedi.gyrshot.layers.gamelayers.GameInfoLayer;
import com.stedi.gyrshot.layers.gamelayers.GameLayer;
import com.stedi.gyrshot.layers.gamelayers.TargetsPointerLayer;
import com.stedi.gyrshot.layers.menus.PickGameMenuLayer;
import com.stedi.gyrshot.layers.menus.StartMenuLayer;
import com.stedi.gyrshot.layers.targets.DecreasesTarget;
import com.stedi.gyrshot.other.SensorController;
import com.stedi.gyrshot.overlay.OverlayView;

public class MainActivity extends CameraActivity implements
        SensorController.SensorListener, OverlayView.Listener, LayersView.OnDrawException {
    private SensorController sensorController;

    private ViewGroup cameraPreviewContainer;
    private LayersView layersView;
    private OverlayView overlayView;

    private GameLayer gameLayer;
    private TargetsPointerLayer pointerLayer;
    private GameInfoLayer gameInfoLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        cameraPreviewContainer = (ViewGroup) findViewById(R.id.main_activity_camera_preview_container);
        layersView = (LayersView) findViewById(R.id.main_activity_layers_view);
        overlayView = (OverlayView) findViewById(R.id.main_activity_overlay_view);

        if (CoreConfig.TOAST_ON_LAYERS_VIEW_EXCEPTION)
            layersView.setOnDrawExceptionListener(this);

        sensorController = new SensorController(this);

        layersView.attachLayerToTheBottom(new ZoneLayer());
        layersView.attachLayerToTheTop(new ShotPointerLayer());
        layersView.addLayer(new StartMenuLayer(), true);
        layersView.setSize(LayersView.Size.SMALL);
        overlayView.setListener(this);
        invalidateOverlayBackButton();
    }

    @Override
    public void onBackPressed() {
        if (layersView.getBackStack().size() <= 1)
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!sensorController.isGyroscopeExist())
            Toast.makeText(this, "Gyroscope sensor not found", Toast.LENGTH_LONG).show();
        if (CoreConfig.ALLOW_ROTATION_SENSOR && !sensorController.isRotationVectorExist())
            Toast.makeText(this, "Rotation sensor not found", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        App.onResume();
        layersView.onResume();
        sensorController.startListening(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorController.stopListening();
        layersView.onPause();
        App.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        layersView.removeAllLayers();
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
    public void fromGyroscope(float xDegree, float yDegree) {
        layersView.updateFromGyroscope(xDegree, yDegree);
    }

    @Override
    public void fromRotationVector(float zDegree) {
        layersView.updateFromRotationVector(zDegree);
    }

    @Override
    public void onDrawException(final Exception ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = ex.getMessage();
                Toast.makeText(MainActivity.this,
                        String.format("LayersView %s", message == null ? "unknown exception" : message),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onShot() {
        ShotCallback callback = layersView.onShot();
        if (callback != null) {
            if (callback instanceof DecreasesTarget.OnShot) {
//                Toast.makeText(this, "Gotcha !", Toast.LENGTH_SHORT).show();
                return;
            }
            if (callback instanceof StartMenuLayer.OnShot) {
                StartMenuLayer.OnShot onShot = (StartMenuLayer.OnShot) callback;
                switch (onShot.type) {
                    case START_GAME:
                        layersView.setSize(LayersView.Size.SMALL);
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
                layersView.setSize(LayersView.Size.BIG);
                gameLayer = new GameLayer(onShot.type);
                pointerLayer = new TargetsPointerLayer();
                gameInfoLayer = new GameInfoLayer();
                gameLayer.addListener(pointerLayer);
                gameLayer.addListener(gameInfoLayer);
                layersView.addLayer(gameLayer, true);
                layersView.addLayer(pointerLayer);
                layersView.addLayer(gameInfoLayer);
                invalidateOverlayBackButton();
            }
        }
    }

    @Override
    public void onBackClick() {
        if (layersView.getBackStack().peek() == gameLayer) {
            gameLayer.removeListener(pointerLayer);
            gameLayer.removeListener(gameInfoLayer);
            layersView.removeLayer(pointerLayer);
            layersView.removeLayer(gameInfoLayer);
            pointerLayer = null;
            gameLayer = null;
        }

        boolean result = layersView.popBackStack();
        invalidateOverlayBackButton();
        if (result) {
            Layer layer = layersView.getBackStack().peek();
            layersView.setSize(layer instanceof GameLayer ? LayersView.Size.BIG : LayersView.Size.SMALL);
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

    private void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
