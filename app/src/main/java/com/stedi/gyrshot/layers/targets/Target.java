package com.stedi.gyrshot.layers.targets;

import android.graphics.Canvas;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.other.FloatRect;

public abstract class Target extends Layer {
    private float x;
    private float y;

    private boolean shouldNotifyOnPause = true;

    protected Target(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract ShotCallback getShotCallback();

    public abstract float getRadius();

    protected abstract void onPauseTarget();

    protected abstract boolean onDrawTarget(Canvas canvas, FloatRect zoneRect, FloatRect actualRect);

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (!App.inOnResume()) {
            if (shouldNotifyOnPause) {
                App.log(this, "onPauseTarget");
                onPauseTarget();
                shouldNotifyOnPause = false;
            }
            return true;
        } else {
            shouldNotifyOnPause = true;
            return onDrawTarget(canvas, zoneRect, actualRect);
        }
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        if (!App.inOnResume())
            return null;
        if (shotX >= getX() - getRadius() && shotX <= getX() + getRadius()
                && shotY >= getY() - getRadius() && shotY <= getY() + getRadius())
            return getShotCallback();
        return null;
    }
}
