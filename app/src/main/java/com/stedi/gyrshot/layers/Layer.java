package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.other.FloatRect;

/**
 * Main class for creating layers for custom drawing with LayersView.
 * If the layer is a child layer, then all these methods should be called by his parent (if it is needed).
 * In a short - a parent is responsible for his children (if any).
 */
public abstract class Layer {
    public abstract void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect);

    public void onAddToLayersView(LayersView layersView) {
    }

    public void onRemoveFromLayersView(LayersView layersView) {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public ShotCallback onShot(float shotX, float shotY) {
        return null;
    }

    public boolean isStatic() {
        return false;
    }
}
