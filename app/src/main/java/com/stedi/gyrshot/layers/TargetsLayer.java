package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.util.Log;

import com.stedi.gyrshot.Mode;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.targets.TargetsFactory;

import java.util.ArrayList;
import java.util.List;

public class TargetsLayer extends Layer {
    private List<Target> targets;

    private void createTargets() {
        targets = new ArrayList<>();
        for (int i = 0; i < 50; i++) // test
            targets.add(TargetsFactory.create(TargetsFactory.Type.DECREASES));
    }

    @Override
    public boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        if (targets == null)
            createTargets();

        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (!target.onDraw(canvas, xOffset, yOffset, mode))
                targets.set(i, TargetsFactory.create(TargetsFactory.Type.DECREASES));
        }

        return true;
    }

    @Override
    public boolean onShot(float x, float y) {
        Log.d(getClass().getSimpleName(), "x=" + x + " y=" + y);
        return false;
    }
}
