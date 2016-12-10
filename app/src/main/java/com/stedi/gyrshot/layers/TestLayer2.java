package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.stedi.gyrshot.Mode;

public class TestLayer2 extends Layer {
    private Paint paint;

    public TestLayer2() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    @Override
    public void onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        canvas.drawCircle(xOffset, yOffset, 50, paint);
    }

    @Override
    public boolean onShot(float x, float y) {
        Log.d(getClass().getSimpleName(), "x=" + x + " y=" + y);
        return false;
    }
}
