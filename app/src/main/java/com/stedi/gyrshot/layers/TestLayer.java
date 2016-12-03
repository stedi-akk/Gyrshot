package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TestLayer extends Layer {
    private Paint paint;

    public TestLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas, float x, float y) {
        canvas.drawRect(x, y, x + 50, y + 50, paint);
    }
}
