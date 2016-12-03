package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class TestLayer2 extends Layer {
    private Paint paint;

    public TestLayer2() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas canvas, float x, float y) {
        canvas.drawRect(x, y, x + 200, y + 200, paint);
    }

    @Override
    public boolean onShot(float x, float y) {
        Log.d(getClass().getSimpleName(), "x=" + x + " y=" + y);
        return false;
    }
}
