package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.util.Log;

import com.stedi.gyrshot.Mode;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.targets.TargetsFactory;

public class TargetsLayer extends Layer {
    private Target target;

    public TargetsLayer() {
    }

    @Override
    public boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        if (target == null)
            target = TargetsFactory.create(TargetsFactory.Type.DECREASES);
        if (!target.onDraw(canvas, xOffset, yOffset, mode))
            target = null;
        return true;
    }

    @Override
    public boolean onShot(float x, float y) {
        Log.d(getClass().getSimpleName(), "x=" + x + " y=" + y);
        return false;
    }
}
