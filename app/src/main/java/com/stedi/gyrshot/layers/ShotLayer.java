package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ShotLayer extends Layer {
    private Paint paint;

    public ShotLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, 20, paint);
    }
}
