package com.stedi.gyrshot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotLayer;

import java.util.ArrayList;
import java.util.List;

public class MainView extends View implements View.OnClickListener {
    private final List<Layer> layers = new ArrayList<>();
    private final ShotLayer shotLayer = new ShotLayer();

    private float x;
    private float y;

    public MainView(Context context) {
        this(context, null);
    }

    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        shotLayer.onDraw(canvas, getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void onClick(View v) {
        boolean invalidate = false;
        for (Layer layer : layers)
            invalidate = layer.onShot(getWidth() / 2, getHeight() / 2);
        if (invalidate)
            invalidate();
    }
}
