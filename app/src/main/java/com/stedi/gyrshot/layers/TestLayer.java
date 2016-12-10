package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TestLayer extends Layer {
    private Paint paint;

    public TestLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas canvas, float xOffset, float yOffset) {
        canvas.drawCircle(xOffset + 200, yOffset + 200, 100, paint);
    }
}
