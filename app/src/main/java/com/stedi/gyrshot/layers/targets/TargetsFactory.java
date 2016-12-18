package com.stedi.gyrshot.layers.targets;

import android.graphics.Rect;

import com.stedi.gyrshot.App;

public class TargetsFactory {
    public enum Type {
        DECREASES,
        FAST_DISAPPEARS,
        MOVABLE,
        MOVABLE_DECREASES
    }

    public static Target create(Type type, Rect rect) {
        if (type == Type.DECREASES) {
            int radius = (int) DecreasesTarget.INITIAL_RADIUS;
            int x = App.rand(rect.left + radius, rect.right - radius);
            int y = App.rand(rect.top + radius, rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
