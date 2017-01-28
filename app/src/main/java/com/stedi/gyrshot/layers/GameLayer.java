package com.stedi.gyrshot.layers;

import android.graphics.Canvas;

import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.targets.TargetsFactory;
import com.stedi.gyrshot.other.FloatRect;

import java.util.ArrayList;
import java.util.List;

public class GameLayer extends Layer {
    private Games.Type type;
    private List<Target> targets;

    public GameLayer(Games.Type type) {
        this.type = type;
    }

    @Override
    public void onResume() {
        for (Target target : targets)
            target.onResume();
    }

    @Override
    public void onPause() {
        for (Target target : targets)
            target.onPause();
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (targets == null) {
            targets = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Target target = TargetsFactory.create(type, actualRect);
                if (target == null)
                    continue;
                targets.add(target);
            }
        }

        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (target != null && target.isAlive())
                target.onDraw(canvas, zoneRect, actualRect);
            else
                targets.set(i, TargetsFactory.create(type, actualRect));
        }
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
