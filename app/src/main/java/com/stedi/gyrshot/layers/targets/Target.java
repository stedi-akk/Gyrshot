package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotCallback;

public abstract class Target extends Layer {
    protected float x;
    protected float y;

    protected Target(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract ShotCallback getShotCallback();

    public abstract float getRadius();

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        if (shotX >= x - getRadius() && shotX <= x + getRadius()
                && shotY >= y - getRadius() && shotY <= y + getRadius())
            return getShotCallback();
        return null;
    }
}
