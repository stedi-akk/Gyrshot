package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.other.FloatRect;

public abstract class Layer {
    public abstract boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect);

    public void onResume() {
    }

    public void onPause() {
    }

    public ShotCallback onShot(float shotX, float shotY) {
        return null;
    }
}
