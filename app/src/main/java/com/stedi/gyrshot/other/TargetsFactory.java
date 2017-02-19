package com.stedi.gyrshot.other;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.GamesConfig;
import com.stedi.gyrshot.layers.targets.DecreasesTarget;
import com.stedi.gyrshot.layers.targets.Target;

public class TargetsFactory {
    public static Target create(GamesConfig.Type type, FloatRect rect) {
        if (type == GamesConfig.Type.DECREASES) {
            float radius = GamesConfig.DECREASES_TARGET_SIZE;
            float x = App.rand(rect.left + radius, rect.right - radius);
            float y = App.rand(rect.top + radius, rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
