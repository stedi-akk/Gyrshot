package com.stedi.gyrshot.layers.targets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.config.GameConfig;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class DecreasesTarget extends Target {
    private static final float INITIAL_RADIUS = GameConfig.DECREASES_TARGET_SIZE;
    private static final long DECREASE_TIME = GameConfig.DECREASES_TARGET_LIFE_TIME;
    private static final float RADIUS_STEP = INITIAL_RADIUS / DECREASE_TIME;

    private final Paint paint = PaintFactory.create(Color.RED);

    private float radius;
    private long firstDrawTime;

    public DecreasesTarget(float x, float y) {
        super(x, y);
    }

    @Override
    public float getRadius() {
        return radius;
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (firstDrawTime != 0) {
            long timeDiff = System.currentTimeMillis() - firstDrawTime;
            if (timeDiff >= DECREASE_TIME)
                return false;
            radius = INITIAL_RADIUS - (RADIUS_STEP * timeDiff);
            canvas.drawCircle(x, y, radius, paint);
            return true;
        } else {
            radius = INITIAL_RADIUS;
            canvas.drawCircle(x, y, radius, paint);
            firstDrawTime = System.currentTimeMillis();
            return true;
        }
    }
}
