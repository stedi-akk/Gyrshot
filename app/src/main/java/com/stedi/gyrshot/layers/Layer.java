package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class Layer {
    public abstract boolean onDraw(Canvas canvas, Rect zoneRect, Rect offsetRect);

    public boolean onShot(float x, float y) {
        return false;
    }
}
