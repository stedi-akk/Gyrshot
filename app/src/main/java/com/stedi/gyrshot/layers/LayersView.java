package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.constants.CoreConfig;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final LayersManager layersManager = LayersManager.getInstance();

    private RefreshThread thread;

    private volatile Mode mode;
    private volatile FloatRect actualRect;

    private volatile float screenHalfWidth, screenHalfHeight;
    private volatile float gyroXOffset, gyroYOffset, rotationZ;
    private volatile boolean isTransparent;
    private volatile boolean isThreadRunning;

    private List<OnNewTranslateValues> onNewTranslateValuesListeners;
    private OnDrawException onExceptionListener;

    public interface OnNewTranslateValues {
        void onGyroXYOffset(float gyroXOffset, float gyroYOffset);

        void onRotationZ(float rotationZ);
    }

    public interface OnDrawException {
        void onDrawException(Exception ex);
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

    public void attachLayerToTheTop(Layer layer) {
        synchronized (layersManager) {
            layersManager.attachLayerToTheTop(layer);
            layer.onAddToLayersView(this);
        }
    }

    public void attachLayerToTheBottom(Layer layer) {
        synchronized (layersManager) {
            layersManager.attachLayerToTheBottom(layer);
            layer.onAddToLayersView(this);
        }
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean addToBackStack) {
        synchronized (layersManager) {
            layersManager.addLayer(layer, addToBackStack);
            layer.onAddToLayersView(this);
        }
    }

    public boolean removeLayer(Layer layer) {
        synchronized (layersManager) {
            boolean result = layersManager.removeLayer(layer);
            if (result)
                layer.onRemoveFromLayersView(this);
            return result;
        }
    }

    public boolean popBackStack() {
        synchronized (layersManager) {
            Layer layer = layersManager.getBackStack().peek();
            boolean result = layersManager.popBackStack();
            if (result)
                layer.onRemoveFromLayersView(this);
            return result;
        }
    }

    public Stack<Layer> getBackStack() {
        return layersManager.getBackStack();
    }

    public void removeAllLayers() {
        synchronized (layersManager) {
            layersManager.clear();
        }
    }

    public void onResume() {
        synchronized (layersManager) {
            for (Layer layer : layersManager.getVisibleLayers())
                layer.onResume();
        }
    }

    public void onPause() {
        synchronized (layersManager) {
            for (Layer layer : layersManager.getVisibleLayers())
                layer.onPause();
        }
    }

    public void setTransparent(boolean value) {
        isTransparent = value;
    }

    public void setOnDrawExceptionListener(OnDrawException onExceptionListener) {
        this.onExceptionListener = onExceptionListener;
    }

    public void addOnNewTranslateValuesListener(OnNewTranslateValues listener) {
        if (onNewTranslateValuesListeners == null)
            onNewTranslateValuesListeners = new ArrayList<>();
        onNewTranslateValuesListeners.add(listener);
    }

    public void removeOnNewTranslateValuesListener(OnNewTranslateValues listener) {
        if (onNewTranslateValuesListeners != null)
            onNewTranslateValuesListeners.remove(listener);
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        gyroXOffset = actualRect.forceInLeftRight(gyroXOffset + gyroX);
        gyroYOffset = actualRect.forceInTopBottom(gyroYOffset + gyroY);

        notifyNewGyroValues();
    }

    public void updateFromRotationVector(float rotationZ) {
        this.rotationZ = rotationZ;

        notifyNewRotationValue();
    }

    public ShotCallback onShot() {
        synchronized (layersManager) {
            List<Layer> layers = layersManager.getVisibleLayers();
            for (ListIterator<Layer> iterator = layers.listIterator(layers.size()); iterator.hasPrevious(); ) {
                ShotCallback callback = iterator.previous().onShot(-gyroXOffset, -gyroYOffset);
                if (callback != null)
                    return callback;
            }
            return null;
        }
    }

    private void calculateActualRect() {
        if (CoreConfig.ATTACH_ZONE_RECT_TO_SCREEN_EDGES) {
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
        if (onNewTranslateValuesListeners != null)
            for (OnNewTranslateValues listener : onNewTranslateValuesListeners)
                listener.onGyroXYOffset(gyroXOffset, gyroYOffset);
    }

    private void notifyNewRotationValue() {
        if (onNewTranslateValuesListeners != null)
            for (OnNewTranslateValues listener : onNewTranslateValuesListeners)
                listener.onRotationZ(rotationZ);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isThreadRunning = true;
        thread = new RefreshThread(holder);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isThreadRunning = false;
        thread.interrupt();
        thread = null;
    }

    private class RefreshThread extends Thread {
        private final SurfaceHolder surfaceHolder;

        private boolean canvasMoved;

        private RefreshThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            while (isThreadRunning) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas == null)
                        continue;

                    clearLastFrame(canvas);
                    drawLayers(canvas);
                } catch (Exception e) {
                    onException(e);
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            onException(e);
                        }
                    }
                }
            }
        }

        private void clearLastFrame(Canvas canvas) {
            if (isTransparent)
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            else
                canvas.drawColor(CoreConfig.LAYERS_VIEW_BACKGROUND_COLOR);
        }

        private void drawLayers(Canvas canvas) {
            // 0,0 point is always in the center of the screen
            canvas.translate(screenHalfWidth, screenHalfHeight);

            // moving canvas for non static layers
            canvasMoved = false;
            canvas.save(Canvas.MATRIX_SAVE_FLAG);

            synchronized (layersManager) {
                for (Layer layer : layersManager.getVisibleLayers()) {
                    if (!canvasMoved && !layer.isStatic()) {
                        canvas.translate(gyroXOffset, gyroYOffset);
                        if (CoreConfig.ALLOW_ROTATION_SENSOR)
                            canvas.rotate(rotationZ);
                        canvasMoved = true;
                    } else if (canvasMoved && layer.isStatic()) {
                        canvas.restore();
                        canvas.save(Canvas.MATRIX_SAVE_FLAG);
                        canvasMoved = false;
                    }

                    layer.onDraw(canvas, mode.getZoneRect(), actualRect);
                }
            }

            canvas.restore();
        }

        private void onException(Exception e) {
            e.printStackTrace();
            if (onExceptionListener != null)
                onExceptionListener.onDrawException(e);
        }
    }
}
