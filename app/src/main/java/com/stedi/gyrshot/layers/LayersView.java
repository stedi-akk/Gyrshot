package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends View {
    private final List<Layer> layers = new ArrayList<>();

    private float gyroX;
    private float gyroY;

    public LayersView(Context context) {
        this(context, null);
    }

    public LayersView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LayersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public void updateFromGyroscope(float gyroX, float gyroY) {
        this.gyroX += gyroX;
        this.gyroY += gyroY;
        invalidate();
    }

    public void shot(float x, float y) {
        for (Layer layer : layers) {
            if (layer.onShot(x, y)) {
                invalidate();
                return;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Layer layer : layers)
            layer.onDraw(canvas, gyroX, gyroY);
    }
}
