package com.stedi.gyrshot.layers.targets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.other.FloatRect;

public class DecreasesTarget extends Target {
    public static final float INITIAL_RADIUS = (int) App.dp2px(50);

    private static final int DECREASE_TIME = 5000;
    private static final float RADIUS_STEP = INITIAL_RADIUS / DECREASE_TIME;
    private static final Paint PAINT;

    private long firstDrawTime;

    static {
        PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
        PAINT.setColor(Color.RED);
    }

    public DecreasesTarget(float x, float y) {
        super(x, y);
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (firstDrawTime != 0) {
            long timeDiff = System.currentTimeMillis() - firstDrawTime;
            if (timeDiff >= DECREASE_TIME)
                return false;
            float radius = INITIAL_RADIUS - (RADIUS_STEP * timeDiff);
            canvas.drawCircle(x, y, radius, PAINT);
            return true;
        } else {
            canvas.drawCircle(x, y, INITIAL_RADIUS, PAINT);
            firstDrawTime = System.currentTimeMillis();
            return true;
        }
    }

    @Override
    public boolean onShot(float x, float y) {
        return super.onShot(x, y);
    }
}
