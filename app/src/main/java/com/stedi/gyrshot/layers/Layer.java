package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.LayersThread;
import com.stedi.gyrshot.other.UiThread;

/**
 * Main class for creating layers for custom drawing with LayersView.
 * If the layer is a child layer, then all these methods should be called by his parent (if it is needed).
 * In a short - a parent is responsible for his children (if any).
 */
public abstract class Layer {
    @LayersThread
    public abstract void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect);

    @UiThread
    public void onAddToLayersView(LayersView layersView) {
    }

    @UiThread
    public void onRemoveFromLayersView(LayersView layersView) {
    }

    @UiThread
    public void onResume() {
    }

    @UiThread
    public void onPause() {
    }

    @UiThread
    public ShotCallback onShot(float shotX, float shotY) {
        return null;
    }

    @LayersThread
    public boolean isStatic() {
        return false;
    }
}
