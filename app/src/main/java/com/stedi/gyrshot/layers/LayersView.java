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
import com.stedi.gyrshot.Tools;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final List<Layer> layers = new ArrayList<>();

    private RefreshThread thread;

    private float gyroXOffset;
    private float gyroYOffset;

    private Paint debugTextPaint;

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
        this.gyroXOffset += gyroX;
        this.gyroYOffset += gyroY;
    }

    public void shot(float x, float y) {
        for (Layer layer : layers) {
            if (layer.onShot(x, y))
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

    private void drawLayers(Canvas canvas) {
        for (Layer layer : layers)
            layer.onDraw(canvas, gyroXOffset, gyroYOffset);
    }

    private void drawDebugInfo(Canvas canvas, String info) {
        Paint paint = getDebugTextPaint();
        paint.setColor(Color.BLACK);
        canvas.drawText(info, paint.getTextSize() + 1, paint.getTextSize() * 2 + 1, paint);
        paint.setColor(Color.LTGRAY);
        canvas.drawText(info, paint.getTextSize(), paint.getTextSize() * 2, paint);
    }

    private Paint getDebugTextPaint() {
        if (debugTextPaint == null) {
            debugTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugTextPaint.setTextSize(Tools.dp2px(getContext(), 15));
            debugTextPaint.setColor(Color.WHITE);
        }
        return debugTextPaint;
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
                            drawDebugInfo(canvas, String.valueOf(fps));
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
