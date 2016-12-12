package com.stedi.gyrshot.targets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Mode;

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

    public DecreasesTarget(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        if (firstDrawTime != 0) {
            long timeDiff = System.currentTimeMillis() - firstDrawTime;
            if (timeDiff >= DECREASE_TIME)
                return false;

            float radius = INITIAL_RADIUS - (RADIUS_STEP * timeDiff);
            canvas.drawCircle(x + xOffset, y + yOffset, radius, PAINT);
            return true;
        } else {
            canvas.drawCircle(x + xOffset, y + yOffset, INITIAL_RADIUS, PAINT);
            firstDrawTime = System.currentTimeMillis();
            return true;
        }
    }

    @Override
    public boolean onShot(float x, float y) {
        return super.onShot(x, y);
    }
}