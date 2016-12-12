package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Config;
import com.stedi.gyrshot.Mode;
import com.stedi.gyrshot.targets.TargetsFactory;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends SurfaceView implements SurfaceHolder.Callback {
    private final List<Layer> layers = new ArrayList<>();

    private RefreshThread thread;
    private Mode mode;

    private float gyroXOffset, gyroYOffset;
    private float centerX, centerY;

    private Paint debugTextPaint;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        TargetsFactory.setMode(mode);
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

        Rect rect = mode.getZoneRect();

        if (gyroXOffset < rect.left)
            gyroXOffset = rect.left;
        else if (gyroXOffset > rect.right)
            gyroXOffset = rect.right;

        if (gyroYOffset < rect.top)
            gyroYOffset = rect.top;
        else if (gyroYOffset > rect.bottom)
            gyroYOffset = rect.bottom;
    }

    public void onShot() {
        for (Layer layer : layers) {
            if (layer.onShot(centerX, centerY))
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
            layer.onDraw(canvas, gyroXOffset, gyroYOffset, mode);
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
            debugTextPaint.setTextSize(App.dp2px(15));
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
                        if (isTransparent)
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        else
                            canvas.drawColor(Config.LAYERS_VIEW_BACKGROUND_COLOR);
                        canvas.save();
                        canvas.translate(centerX, centerY);
                        drawLayers(canvas);
                        canvas.restore();
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
