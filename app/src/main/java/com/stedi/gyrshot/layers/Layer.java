package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

public abstract class Layer {
    public abstract void onDraw(Canvas canvas, float xOffset, float yOffset);

    public boolean onShot(float x, float y) {
        return false;
    }
}
