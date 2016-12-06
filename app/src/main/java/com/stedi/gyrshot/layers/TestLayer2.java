package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class TestLayer2 extends Layer {
    private Paint paint;

    public TestLayer2() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas canvas, float gyroX, float gyroY) {
        canvas.drawCircle(gyroX + 400, gyroY + 800, 100, paint);
    }

    @Override
    public boolean onShot(float x, float y) {
        Log.d(getClass().getSimpleName(), "x=" + x + " y=" + y);
        return false;
    }
}
