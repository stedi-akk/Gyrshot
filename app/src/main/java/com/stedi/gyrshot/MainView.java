package com.stedi.gyrshot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.stedi.gyrshot.layers.Layer;

import java.util.ArrayList;
import java.util.List;

public class MainView extends View {
    private final List<Layer> layers = new ArrayList<>();

    private float x;
    private float y;

    public MainView(Context context) {
        super(context);
    }

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public void update(float gyroX, float gyroY) {
        this.x += gyroX;
        this.y += gyroY;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Layer layer : layers)
            layer.onDraw(canvas, x, y);
    }
}
