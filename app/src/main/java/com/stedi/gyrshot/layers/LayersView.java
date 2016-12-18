package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.Config;
import com.stedi.gyrshot.Mode;
import com.stedi.gyrshot.layers.targets.TargetsFactory;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final List<Layer> layers = new ArrayList<>();
    private DebugLayer debugLayer;

    private RefreshThread thread;
    private Mode mode;

    private Rect offsetRect;

    private float gyroXOffset, gyroYOffset;
    private float centerX, centerY;
    private float shotX, shotY;

    private boolean isTransparent;

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
        centerX = w / 2;
        centerY = h / 2;
        updateOffsetRect();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        updateOffsetRect();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public void setTransparent(boolean value) {
        isTransparent = value;
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        gyroXOffset += gyroX;
        gyroYOffset += gyroY;

        if (gyroXOffset < offsetRect.left)
            gyroXOffset = offsetRect.left;
        else if (gyroXOffset > offsetRect.right)
            gyroXOffset = offsetRect.right;

        if (gyroYOffset < offsetRect.top)
            gyroYOffset = offsetRect.top;
        else if (gyroYOffset > offsetRect.bottom)
            gyroYOffset = offsetRect.bottom;
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

    private void updateOffsetRect() {
        if (Config.ATTACH_ZONE_RECT_TO_SCREEN_EDGES) {
            Rect rect = mode.getZoneRect();
            int leftEdge = (int) (rect.left + centerX);
            int rightEdge = (int) (rect.right - centerX);
            int topEdge = (int) (rect.top + centerY);
            int bottomEdge = (int) (rect.bottom - centerY);
            offsetRect = new Rect(leftEdge, topEdge, rightEdge, bottomEdge);
        } else {
            offsetRect = mode.getZoneRect();
        }
        onOffsetRectUpdated();
    }

    private void onOffsetRectUpdated() {
        TargetsFactory.setCreationRect(offsetRect);
    }

    private void drawLayers(Canvas canvas) {
        for (Layer layer : layers)
            layer.onDraw(canvas, gyroXOffset, gyroYOffset, mode);
    }

    private void drawDebugLayer(Canvas canvas, String debugText) {
        debugLayer.showOffsetRect(offsetRect);
        debugLayer.showDebugText(debugText);
        debugLayer.showLastShot(shotX, shotY);
        debugLayer.onDraw(canvas, gyroXOffset, gyroYOffset, mode);
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
            int fps = 0;

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
                        canvas.translate(centerX, centerY);

                        if (isTransparent)
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        else
                            canvas.drawColor(Config.LAYERS_VIEW_BACKGROUND_COLOR);

                        drawLayers(canvas);

                        if (Config.SHOW_DEBUG_LAYER)
                            drawDebugLayer(canvas, String.valueOf(fps));
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
