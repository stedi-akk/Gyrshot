package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.targets.TargetsFactory;
import com.stedi.gyrshot.other.FloatRect;

import java.util.ArrayList;
import java.util.List;

public class TargetsLayer extends Layer {
    private List<Target> targets;

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (targets == null) {
            targets = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                targets.add(TargetsFactory.create(Games.Type.DECREASES, actualRect));
        }

        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (target == null || !target.onDraw(canvas, zoneRect, actualRect))
                targets.set(i, TargetsFactory.create(Games.Type.DECREASES, actualRect));
        }

        return true;
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (target != null) {
                ShotCallback callback = target.onShot(shotX, shotY);
                if (callback != null) {
                    targets.set(i, null);
                    return callback;
                }
            }
        }
        return null;
    }
}
