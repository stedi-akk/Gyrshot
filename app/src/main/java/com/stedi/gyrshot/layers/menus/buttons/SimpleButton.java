package com.stedi.gyrshot.layers.menus.buttons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.other.FloatRect;

public class SimpleButton extends Layer {
    private static final float WIDTH = App.dp2px(300);
    private static final float HEIGHT = App.dp2px(60);
    private static final float PADDING = App.dp2px(2);

    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private FloatRect boundsRect;
    private FloatRect drawRect;

    private int id;
    private CharSequence text;
    private ShotCallback callback;

    private float xOffset, yOffset;

    public interface ShotCallback {
        void onButtonShot(int id);
    }

    public SimpleButton(int id, CharSequence text, ShotCallback callback) {
        this.id = id;
        this.text = text;
        this.callback = callback;
        init();
    }

    public void setXYOffset(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        calculateBoundsRect();
        calculateDrawRect();
    }

    public FloatRect getBoundsRect() {
        return boundsRect;
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom, fillPaint);
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom, borderPaint);
        canvas.drawText(text, 0, text.length(), xOffset, yOffset, textPaint);
        return true;
    }

    @Override
    public boolean onShot(float shotX, float shotY) {
        return super.onShot(shotX, shotY);
    }

    private void init() {
        fillPaint.setColor(Color.WHITE);

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1f);
        borderPaint.setColor(Color.BLACK);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(HEIGHT / 2);

        calculateBoundsRect();
        calculateDrawRect();
    }

    private void calculateBoundsRect() {
        if (boundsRect == null)
            boundsRect = new FloatRect(WIDTH, HEIGHT);
        boundsRect = new FloatRect(boundsRect.left + xOffset, boundsRect.top + yOffset,
                boundsRect.right + xOffset, boundsRect.bottom + yOffset);
    }

    private void calculateDrawRect() {
        drawRect = new FloatRect(boundsRect.left + PADDING, boundsRect.top + PADDING,
                boundsRect.right - PADDING, boundsRect.bottom - PADDING);
    }
}
