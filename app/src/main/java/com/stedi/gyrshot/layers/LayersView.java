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

import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final LayersManager layersManager = LayersManager.getInstance();

    private DebugLayer debugLayer;

    private RefreshThread thread;
    private Mode mode;
    private FloatRect actualRect;

    private float screenHalfWidth, screenHalfHeight;
    private float gyroXOffset, gyroYOffset;
    private float shotX, shotY;

    private boolean isTransparent;

    private int fps;

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
        layersManager.addLayer(layer);
    }

    public boolean removeLayer(Layer layer) {
        return layersManager.removeLayer(layer);
    }

    public List<Layer> getLayers() {
        return layersManager.getLayers();
    }

    public void setTransparent(boolean value) {
        isTransparent = value;
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        gyroXOffset += gyroX;
        gyroYOffset += gyroY;

        if (gyroXOffset < actualRect.left)
            gyroXOffset = actualRect.left;
        else if (gyroXOffset > actualRect.right)
            gyroXOffset = actualRect.right;

        if (gyroYOffset < actualRect.top)
            gyroYOffset = actualRect.top;
        else if (gyroYOffset > actualRect.bottom)
            gyroYOffset = actualRect.bottom;
    }

    public ShotCallback onShot() {
        shotX = -gyroXOffset;
        shotY = -gyroYOffset;
        for (Layer layer : layersManager.getLayers()) {
            ShotCallback callback = layer.onShot(shotX, shotY);
            if (callback != null)
                return callback;
        }
        return null;
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

    private void drawLayers(Canvas canvas) {
        for (Layer layer : layersManager.getLayers())
            layer.onDraw(canvas, mode.getZoneRect(), actualRect);
    }

    private void drawDebugLayer(Canvas canvas) {
        debugLayer.showZoneRect(AppConfig.DEBUG_LAYER_SHOW_ZONE_RECT);
        debugLayer.showActualRect(AppConfig.DEBUG_LAYER_SHOW_ACTUAL_RECT);

        if (AppConfig.DEBUG_LAYER_SHOW_DEBUG_TEXT) {
            debugLayer.clearDebugText();
            debugLayer.prepareDebugText(gyroXOffset, gyroYOffset);
            debugLayer.addDebugText("fps: " + String.valueOf(fps));
            debugLayer.addDebugText("gyroXOffset: " + String.valueOf(gyroXOffset));
            debugLayer.addDebugText("gyroYOffset: " + String.valueOf(gyroYOffset));
        }

        if (AppConfig.DEBUG_LAYER_SHOW_LAST_SHOT)
            debugLayer.showLastShot(shotX, shotY);

        debugLayer.onDraw(canvas, mode.getZoneRect(), actualRect);
    }

    private class RefreshThread extends Thread {
        private final SurfaceHolder surfaceHolder;

        private boolean run = true;

        private RefreshThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            long sleepPerFrame = 0;
            if (AppConfig.LAYERS_VIEW_MAX_FPS > 0)
                sleepPerFrame = 1000 / AppConfig.LAYERS_VIEW_MAX_FPS;

            if (AppConfig.SHOW_DEBUG_LAYER && debugLayer == null)
                debugLayer = new DebugLayer();

            boolean countFps = AppConfig.SHOW_DEBUG_LAYER && AppConfig.DEBUG_LAYER_SHOW_DEBUG_TEXT;
            long lastFrameTime = 0;
            int framesCount = 0;

            while (run) {
                Canvas canvas = null;
                try {
                    if (sleepPerFrame > 0)
                        sleep(sleepPerFrame);

                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder) {
                        // translate canvas to the center, and move it by gyroscope offset values
                        canvas.translate(screenHalfWidth + gyroXOffset, screenHalfHeight + gyroYOffset);

                        // clear last frame
                        if (isTransparent)
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        else
                            canvas.drawColor(AppConfig.LAYERS_VIEW_BACKGROUND_COLOR);

                        // main draw logic
                        drawLayers(canvas);

                        // draw debug info with special layer
                        if (AppConfig.SHOW_DEBUG_LAYER)
                            drawDebugLayer(canvas);

                        // fps count
                        if (countFps) {
                            framesCount++;
                            long frameTime = System.currentTimeMillis();
                            if (frameTime - lastFrameTime >= 1000) {
                                lastFrameTime = frameTime;
                                fps = framesCount;
                                framesCount = 0;
                            }
                        }
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

        private void stopThread() {
            run = false;
            interrupt();
        }
    }
}
