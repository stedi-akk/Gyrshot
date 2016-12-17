package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.layers.Layer;

public abstract class Target extends Layer {
    protected int x;
    protected int y;

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
