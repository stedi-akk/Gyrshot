package com.stedi.gyrshot.targets;

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
            int x = App.rand(sMode.rect.left + radius, sMode.rect.right - radius);
            int y = App.rand(sMode.rect.top + radius, sMode.rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
