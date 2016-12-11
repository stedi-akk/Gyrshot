package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.Mode;

public abstract class Layer {
    public abstract boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode);

    public boolean onShot(float x, float y) {
        return false;
    }
}
