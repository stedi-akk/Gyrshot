package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.config.GameConfig;
import com.stedi.gyrshot.other.FloatRect;

public class TargetsFactory {
    public enum Type {
        DECREASES,
        FAST_DISAPPEARS,
        MOVABLE,
        MOVABLE_DECREASES
    }

    public static Target create(Type type, FloatRect rect) {
        if (type == Type.DECREASES) {
            float radius = GameConfig.DECREASES_TARGET_SIZE;
            float x = App.rand(rect.left + radius, rect.right - radius);
            float y = App.rand(rect.top + radius, rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
