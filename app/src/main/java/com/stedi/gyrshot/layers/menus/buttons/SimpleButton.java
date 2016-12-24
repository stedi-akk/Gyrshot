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

    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private FloatRect rect = new FloatRect(WIDTH, HEIGHT);

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
        createNewRect();
    }

    public FloatRect getRect() {
        return rect;
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, borderPaint);
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
        borderPaint.setStrokeWidth(App.dp2px(1));
        borderPaint.setColor(Color.BLACK);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(HEIGHT / 2);
    }

    private void createNewRect() {
        float left = rect.left + xOffset;
        float top = rect.top + yOffset;
        float right = rect.right + xOffset;
        float bottom = rect.bottom + yOffset;
        rect = new FloatRect(left, top, right, bottom);
    }
}
