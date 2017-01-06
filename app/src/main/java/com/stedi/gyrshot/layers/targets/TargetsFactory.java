package com.stedi.gyrshot.layers.targets;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.other.FloatRect;

public class TargetsFactory {
    public static Target create(Games.Type type, FloatRect rect) {
        if (type == Games.Type.DECREASES) {
            float radius = Games.DECREASES_TARGET_SIZE;
            float x = App.rand(rect.left + radius, rect.right - radius);
            float y = App.rand(rect.top + radius, rect.bottom - radius);
            return new DecreasesTarget(x, y);
        }
        return null;
    }
}
