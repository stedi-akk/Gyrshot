package com.stedi.gyrshot.layers.targets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.constants.GamesConfig;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class DecreasesTarget extends Target {
    private static final float RADIUS_STEP = GamesConfig.DECREASES_TARGET_SIZE / GamesConfig.DECREASES_TARGET_LIFE_TIME;

    public class OnShot implements ShotCallback {
    }

    private final Paint paint = PaintFactory.create(Color.RED);

    private float targetRadius;

    private long startTime;
    private long elapsedTime;

    private boolean isAlive = true;
    private boolean onPauseCalled;

    public DecreasesTarget(float x, float y) {
        super(x, y);
    }

    @Override
    public ShotCallback getShotCallback() {
        return new OnShot();
    }

    @Override
    public float getRadius() {
        return targetRadius;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    protected void onPauseTarget() {
        onPauseCalled = true;
    }

    @Override
    protected void onDrawTarget(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (startTime == 0)
            startTime = System.currentTimeMillis();

        if (onPauseCalled) {
            startTime = System.currentTimeMillis() - elapsedTime;
            onPauseCalled = false;
        }

        elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime >= GamesConfig.DECREASES_TARGET_LIFE_TIME)
            isAlive = false;

        targetRadius = GamesConfig.DECREASES_TARGET_SIZE - (RADIUS_STEP * elapsedTime);
        canvas.drawCircle(getX(), getY(), targetRadius, paint);
    }
}
