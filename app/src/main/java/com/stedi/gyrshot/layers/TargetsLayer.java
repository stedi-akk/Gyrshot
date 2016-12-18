package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.targets.TargetsFactory;

import java.util.ArrayList;
import java.util.List;

public class TargetsLayer extends Layer {
    private List<Target> targets;

    @Override
    public boolean onDraw(Canvas canvas, Rect zoneRect, Rect offsetRect) {
        if (targets == null) {
            targets = new ArrayList<>();
            for (int i = 0; i < 50; i++)
                targets.add(TargetsFactory.create(TargetsFactory.Type.DECREASES, offsetRect));
        }

        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (!target.onDraw(canvas, zoneRect, offsetRect))
                targets.set(i, TargetsFactory.create(TargetsFactory.Type.DECREASES, offsetRect));
        }

        return true;
    }

    @Override
    public boolean onShot(float x, float y) {
        return false;
    }
}
