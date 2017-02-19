package com.stedi.gyrshot.layers.views;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.other.FloatRect;

public class TextLayer extends Layer {
    private final Paint textPaint;

    private String text;

    private float requestedX, requestedY;

    private float[] textPosition;

    public TextLayer(Paint textPaint) {
        this.textPaint = textPaint;
        this.textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(float x, float y) {
        if (requestedX == x && requestedY == y)
            return;

        requestedX = x;
        requestedY = y;
        textPosition = null;
    }

    public float[] getTextPosition() {
        return textPosition;
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (text == null)
            return;

        if (textPosition == null) {
            int textHeight = App.getTextHeight(text, textPaint);
            textPosition = new float[]{requestedX, requestedY + textHeight / 2};
        }

        canvas.drawText(text, textPosition[0], textPosition[1], textPaint);
    }
}
