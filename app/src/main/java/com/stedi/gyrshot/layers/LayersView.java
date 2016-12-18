package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.Config;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.Mode;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private List<Layer> layers;
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
        if (layers == null)
            layers = new ArrayList<>();
        layers.add(layer);
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

    public void onShot() {
        shotX = -gyroXOffset;
        shotY = -gyroYOffset;
        for (Layer layer : layers) {
            if (layer.onShot(shotX, shotY))
                return;
        }
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
        if (Config.ATTACH_ZONE_RECT_TO_SCREEN_EDGES) {
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
        for (Layer layer : layers)
            layer.onDraw(canvas, mode.getZoneRect(), actualRect);
    }

    private void drawDebugLayer(Canvas canvas) {
        debugLayer.showZoneRect(true);
        debugLayer.showActualRect(true);
        debugLayer.showDebugText(String.valueOf(fps));
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
            long refreshTime = System.currentTimeMillis();

            int framesCount = 0;

            while (run) {
                Canvas canvas = null;
                try {
                    sleep(1000 / Config.LAYERS_VIEW_FPS);

                    if (Config.SHOW_DEBUG_LAYER) {
                        if (debugLayer == null)
                            debugLayer = new DebugLayer();

                        framesCount++;
                        if (System.currentTimeMillis() - refreshTime >= 1000) {
                            refreshTime = System.currentTimeMillis();
                            fps = framesCount;
                            framesCount = 0;
                        }
                    }

                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder) {
                        canvas.translate(screenHalfWidth + gyroXOffset, screenHalfHeight + gyroYOffset);

                        if (isTransparent)
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        else
                            canvas.drawColor(Config.LAYERS_VIEW_BACKGROUND_COLOR);

                        drawLayers(canvas);
                        if (Config.SHOW_DEBUG_LAYER)
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

        private void stopThread() {
            run = false;
            interrupt();
        }
    }
}
