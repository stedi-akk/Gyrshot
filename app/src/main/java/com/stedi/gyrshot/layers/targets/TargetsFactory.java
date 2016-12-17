package com.stedi.gyrshot.layers.targets;

import android.graphics.Rect;

import com.stedi.gyrshot.App;

public class TargetsFactory {
    private static Rect creationRect;

    public enum Type {
        DECREASES,
        FAST_DISAPPEARS,
        MOVABLE,
        MOVABLE_DECREASES
    }

    public static void setCreationRect(Rect rect) {
        creationRect = rect;
    }

    public static Target create(Type type) {
        if (type == Type.DECREASES) {
            int radius = (int) DecreasesTarget.INITIAL_RADIUS;
            int x = App.rand(creationRect.left + radius, creationRect.right - radius);
            int y = App.rand(creationRect.top + radius, creationRect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
