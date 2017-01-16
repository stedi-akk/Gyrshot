package com.stedi.gyrshot.layers.targets;

import android.graphics.Canvas;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.other.FloatRect;

public abstract class Target extends Layer {
    private float x;
    private float y;

    private boolean inOnResume = App.inOnResume();

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
    public void onResume() {
        inOnResume = true;
    }

    @Override
    public void onPause() {
        inOnResume = false;
        onPauseTarget();
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (inOnResume)
            return onDrawTarget(canvas, zoneRect, actualRect);
        return true;
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        if (inOnResume && shotX >= getX() - getRadius() && shotX <= getX() + getRadius()
                && shotY >= getY() - getRadius() && shotY <= getY() + getRadius())
            return getShotCallback();
        return null;
    }
}
