package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.layers.Layer;

public abstract class Target extends Layer {
    protected float x;
    protected float y;

    public Target(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
