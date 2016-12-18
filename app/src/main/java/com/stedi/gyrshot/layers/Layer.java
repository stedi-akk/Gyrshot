package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.other.FloatRect;

public abstract class Layer {
    public abstract boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect);

    public boolean onShot(float x, float y) {
        return false;
    }
}
