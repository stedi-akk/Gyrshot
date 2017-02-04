package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class ShotPointerLayer extends Layer {
    private final Paint shotPaint = PaintFactory.create(Color.YELLOW);

    private float shotX, shotY;

    private boolean shot;

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (shot)
            canvas.drawCircle(shotX, shotY, 10, shotPaint);
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        shot = true;
        this.shotX = shotX;
        this.shotY = shotY;
        return null;
    }
}
