package com.stedi.gyrshot.layers.gamelayers;

import android.graphics.Canvas;

import com.stedi.gyrshot.constants.GamesConfig;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.TargetsFactory;

import java.util.ArrayList;
import java.util.List;

public class GameLayer extends Layer {
    private GamesConfig.Type type;
    private List<Target> targets;

    private List<TargetsListener> listeners;

    public interface TargetsListener {
        void onNewTarget(Target target);

        void onDrawTargets(List<Target> targets);

        void onTargetDelete(Target target, boolean fromShot);
    }

    public GameLayer(GamesConfig.Type type) {
        this.type = type;
    }

    public void addListener(TargetsListener listener) {
        if (listeners == null)
            listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void removeListener(TargetsListener listener) {
        if (listeners != null)
            listeners.remove(listener);
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
                notifyNew(target);
            }
        }

        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (target != null) {
                if (target.isAlive()) {
                    target.onDraw(canvas, zoneRect, actualRect);
                } else {
                    targets.set(i, TargetsFactory.create(type, actualRect));
                    notifyDelete(target, false);
                }
            } else {
                targets.set(i, TargetsFactory.create(type, actualRect));
            }
        }

        notifyOnDraw();
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        for (int i = 0; i < targets.size(); i++) {
            Target target = targets.get(i);
            if (target != null) {
                ShotCallback callback = target.onShot(shotX, shotY);
                if (callback != null) {
                    targets.set(i, null);
                    notifyDelete(target, true);
                    return callback;
                }
            }
        }
        return null;
    }

    private void notifyNew(Target target) {
        if (listeners != null)
            for (TargetsListener listener : listeners)
                listener.onNewTarget(target);
    }

    private void notifyOnDraw() {
        if (listeners != null)
            for (TargetsListener listener : listeners)
                listener.onDrawTargets(targets);
    }

    private void notifyDelete(Target target, boolean fromShot) {
        if (listeners != null)
            for (TargetsListener listener : listeners)
                listener.onTargetDelete(target, fromShot);
    }
}
