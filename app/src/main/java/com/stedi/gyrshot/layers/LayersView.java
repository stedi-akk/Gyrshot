package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.Config;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final List<Layer> layers = new ArrayList<>();

    private LayersThread thread;

    private float gyroX;
    private float gyroY;

    private Paint fpsPaint;

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

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        this.gyroX += gyroX;
        this.gyroY += gyroY;
    }

    public void shot(float x, float y) {
        for (Layer layer : layers) {
            if (layer.onShot(x, y))
                return;
        }
    }

    private void drawLayers(Canvas canvas) {
        for (Layer layer : layers)
            layer.onDraw(canvas, gyroX, gyroY);
    }

    private void drawFps(Canvas canvas, int fps) {
        Paint paint = getFpsPaint();
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(fps), 101, 101, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(String.valueOf(fps), 100, 100, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new LayersThread(holder);
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

    private class LayersThread extends Thread {
        private final SurfaceHolder surfaceHolder;

        private boolean run = true;

        public LayersThread(SurfaceHolder surfaceHolder) {
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
                    sleep(1000 / Config.LAYERS_MAX_FPS);

                    if (Config.SHOW_FPS) {
                        framesCount++;
                        if (System.currentTimeMillis() - refreshTime >= 1000) {
                            refreshTime = System.currentTimeMillis();
                            fps = framesCount;
                            framesCount = 0;
                        }
                    }

                    canvas = surfaceHolder.lockCanvas();

                    synchronized (surfaceHolder) {
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        drawLayers(canvas);
                        if (Config.SHOW_FPS)
                            drawFps(canvas, fps);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        public void stopThread() {
            run = false;
            interrupt();
        }
    }

    private Paint getFpsPaint() {
        if (fpsPaint == null) {
            fpsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            fpsPaint.setTextSize(100);
            fpsPaint.setColor(Color.WHITE);
        }
        return fpsPaint;
    }
}
