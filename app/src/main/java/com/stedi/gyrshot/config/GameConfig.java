package com.stedi.gyrshot.config;

import com.stedi.gyrshot.App;

import java.util.concurrent.TimeUnit;

/**
 * Game config params (tragets sizes, times, points, etc)
 */
public final class GameConfig {
    public static final float DECREASES_TARGET_SIZE = App.dp2px(50);
    public static final long DECREASES_TARGET_LIFE_TIME = TimeUnit.SECONDS.toMillis(5);
}
