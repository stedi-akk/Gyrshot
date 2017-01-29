package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.constants.AppConfig;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final LayersManager layersManager = LayersManager.getInstance();

    private DebugLayer debugLayer;

    private RefreshThread thread;
    private Mode mode;
    private FloatRect actualRect;

    private float screenHalfWidth, screenHalfHeight;
    private float gyroXOffset, gyroYOffset, rotationZ;
    private float shotX, shotY;

    private boolean isTransparent;

    private List<OnSensorValues> onSensorListeners;

    public interface OnSensorValues {
        void onGyroXYOffset(float gyroXOffset, float gyroYOffset);

        void onRotationZ(float rotationZ);
    }

    public LayersView(Context context) {
        this(context, null);
    }

    public LayersView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LayersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderMediaOverlay(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenHalfWidth = w / 2;
        screenHalfHeight = h / 2;
        calculateActualRect();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        calculateActualRect();
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean addToBackStack) {
        layersManager.addLayer(layer, addToBackStack);
    }

    public boolean removeLayer(Layer layer) {
        return layersManager.removeLayer(layer);
    }

    public boolean popBackStack() {
        return layersManager.popBackStack();
    }

    public Stack<Layer> getBackStack() {
        return layersManager.getBackStack();
    }

    public void onResume() {
        for (Layer layer : layersManager.getVisibleLayers())
            layer.onResume();
    }

    public void onPause() {
        for (Layer layer : layersManager.getVisibleLayers())
            layer.onPause();
    }

    public void setTransparent(boolean value) {
        isTransparent = value;
    }

    public void addOnSensorValuesListener(OnSensorValues listener) {
        if (onSensorListeners == null)
            onSensorListeners = new ArrayList<>();
        onSensorListeners.add(listener);
    }

    public void removeOnSensorValuesListener(OnSensorValues listener) {
        if (onSensorListeners != null)
            onSensorListeners.remove(listener);
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        gyroXOffset += gyroX;
        gyroYOffset += gyroY;

        gyroXOffset = actualRect.forceInLeftRight(gyroXOffset);
        gyroYOffset = actualRect.forceInTopBottom(gyroYOffset);

        notifyNewGyroValues();
    }

    public void updateFromRotationVector(float rotationZ) {
        this.rotationZ = rotationZ;
        notifyNewRotationValue();
    }

    public ShotCallback onShot() {
        shotX = -gyroXOffset;
        shotY = -gyroYOffset;
        for (Layer layer : layersManager.getVisibleLayers()) {
            ShotCallback callback = layer.onShot(shotX, shotY);
            if (callback != null)
                return callback;
        }
        return null;
    }

    private void calculateActualRect() {
        if (AppConfig.ATTACH_ZONE_RECT_TO_SCREEN_EDGES) {
            FloatRect rect = mode.getZoneRect();
            float leftEdge = rect.left + screenHalfWidth;
            float rightEdge = rect.right - screenHalfWidth;
            float topEdge = rect.top + screenHalfHeight;
            float bottomEdge = rect.bottom - screenHalfHeight;
            actualRect = new FloatRect(leftEdge, topEdge, rightEdge, bottomEdge);
        } else {
            actualRect = mode.getZoneRect();
        }
    }

    private void notifyNewGyroValues() {
        if (onSensorListeners != null)
            for (OnSensorValues listener : onSensorListeners)
                listener.onGyroXYOffset(gyroXOffset, gyroYOffset);
    }

    private void notifyNewRotationValue() {
        if (onSensorListeners != null)
            for (OnSensorValues listener : onSensorListeners)
                listener.onRotationZ(rotationZ);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new RefreshThread(holder);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.stopThread();
        thread = null;
    }

    private class RefreshThread extends Thread {
        private final SurfaceHolder surfaceHolder;

        private boolean run = true;

        private RefreshThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            while (run) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        clearLastFrame(canvas);
                        drawLayersAndMoveCanvas(canvas);
                        if (AppConfig.SHOW_DEBUG_LAYER)
                            drawDebugLayer(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private void clearLastFrame(Canvas canvas) {
            if (isTransparent)
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            else
                canvas.drawColor(AppConfig.LAYERS_VIEW_BACKGROUND_COLOR);
        }

        private void drawLayersAndMoveCanvas(Canvas canvas) {
            moveCanvasToTheCenter(canvas);
            for (Layer layer : layersManager.getVisibleLayers()) {
                canvas.save();
                if (!layer.isStatic())
                    moveCanvasBySensors(canvas);
                layer.onDraw(canvas, mode.getZoneRect(), actualRect);
                canvas.restore();
            }
        }

        private void moveCanvasToTheCenter(Canvas canvas) {
            canvas.translate(screenHalfWidth, screenHalfHeight);
        }

        private void moveCanvasBySensors(Canvas canvas) {
            canvas.translate(gyroXOffset, gyroYOffset);
            if (AppConfig.ALLOW_ROTATION_SENSOR)
                canvas.rotate(rotationZ);
        }

        private void drawDebugLayer(Canvas canvas) {
            if (debugLayer == null)
                debugLayer = new DebugLayer();

            moveCanvasBySensors(canvas);

            debugLayer.showZoneRect(AppConfig.DEBUG_LAYER_SHOW_ZONE_RECT);
            debugLayer.showActualRect(AppConfig.DEBUG_LAYER_SHOW_ACTUAL_RECT);

            if (AppConfig.DEBUG_LAYER_SHOW_DEBUG_TEXT) {
                debugLayer.clearDebugText();
                debugLayer.prepareDebugText(gyroXOffset, gyroYOffset);
                debugLayer.addDebugText("gyroXOffset: " + String.valueOf(gyroXOffset));
                debugLayer.addDebugText("gyroYOffset: " + String.valueOf(gyroYOffset));
                debugLayer.addDebugText("rotationZ: " + String.valueOf(rotationZ));
            }

            if (AppConfig.DEBUG_LAYER_SHOW_LAST_SHOT)
                debugLayer.showLastShot(shotX, shotY);

            debugLayer.onDraw(canvas, mode.getZoneRect(), actualRect);
        }

        private void stopThread() {
            run = false;
            interrupt();
        }
    }
}
