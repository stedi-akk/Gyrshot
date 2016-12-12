package com.stedi.gyrshot.targets;

import android.graphics.Rect;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Mode;

public class TargetsFactory {
    private static Mode sMode;

    public enum Type {
        DECREASES,
        FAST_DISAPPEARS,
        MOVABLE,
        MOVABLE_DECREASES
    }

    public static void setMode(Mode mode) {
        sMode = mode;
    }

    public static Target create(Type type) {
        if (type == Type.DECREASES) {
            int radius = (int) DecreasesTarget.INITIAL_RADIUS;
            Rect rect = sMode.getZoneRect();
            int x = App.rand(rect.left + radius, rect.right - radius);
            int y = App.rand(rect.top + radius, rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
