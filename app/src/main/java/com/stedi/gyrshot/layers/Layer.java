package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

public abstract class Layer {
    public abstract void onDraw(Canvas canvas, float x, float y);

    public boolean onShot(float x, float y) {
        return false;
    }
}
