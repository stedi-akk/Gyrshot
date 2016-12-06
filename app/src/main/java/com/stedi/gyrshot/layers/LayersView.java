package com.stedi.gyrshot.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LayersView extends View implements View.OnClickListener {
    private final List<Layer> layers = new ArrayList<>();

    private float x;
    private float y;

    public LayersView(Context context) {
        this(context, null);
    }

    public LayersView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LayersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        boolean invalidate = false;
        for (Layer layer : layers) {
            invalidate = layer.onShot(getWidth() / 2, getHeight() / 2);
            if (invalidate) {
                invalidate();
                break;
            }
        }
    }
}
