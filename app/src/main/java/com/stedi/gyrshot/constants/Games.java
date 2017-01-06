package com.stedi.gyrshot.constants;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.R;

import java.util.concurrent.TimeUnit;

/**
 * Game types and config params (tragets sizes, times, points, etc)
 */
public final class Games {
    public enum Type {
        DECREASES(1001, R.string.decreases_game),
        FAST_DISAPPEARS(1002, R.string.fast_disappears_game),
        MOVABLE(1003, R.string.movable_game),
        MOVABLE_DECREASES(1004, R.string.movable_decreases_game);

        public final int id;
        public final int resTitle;

        Type(int id, int resString) {
            this.id = id;
            this.resTitle = resString;
        }

        public static Type find(int id) {
            for (Type type : Type.values()) {
                if (type.id == id)
                    return type;
            }
            return null;
        }
    }

    public static final float DECREASES_TARGET_SIZE = App.dp2px(50);
    public static final long DECREASES_TARGET_LIFE_TIME = TimeUnit.SECONDS.toMillis(5);
}
