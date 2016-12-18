package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.layers.Layer;

public abstract class Target extends Layer {
    protected float x;
    protected float y;

    protected Target(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract float getRadius();

    @Override
    public boolean onShot(float shotX, float shotY) {
        return shotX >= x - getRadius() && shotX <= x + getRadius()
                && shotY >= y - getRadius() && shotY <= y + getRadius();
    }
}
