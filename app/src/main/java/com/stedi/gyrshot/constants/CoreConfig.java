package com.stedi.gyrshot.constants;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.concurrent.TimeUnit;

/**
 * Application control params
 */
public final class CoreConfig {
    // core
    public static final boolean ALLOW_CAMERA = false;
    public static final boolean ALLOW_ROTATION_SENSOR = false; // experimental and not finished yet
    public static final float GYROSCOPE_SENSOR_ACCURACY = 0.02f;
    public static final float ROTATION_SENSOR_ACCURACY = 0.005f;

    // zone size
    public static final boolean MODE_ZONE_SIZE_DEPENDS_ON_SCREEN = false;
    public static final boolean ATTACH_ZONE_RECT_TO_SCREEN_EDGES = false;
    public static final float[] MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{2.6f, 1.8f};
    public static final float[] GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{4f, 4f};
    public static final int[] STATIC_MENU_SIZE = new int[]{500, 500};
    public static final int[] STATIC_GAME_SIZE = new int[]{700, 700};

    // debug info
    public static final boolean DEBUG_LAYERS_MANAGER = false;
    public static final boolean TOAST_ON_LAYERS_VIEW_EXCEPTION = true;
    public static final boolean DEBUG_ZONE_LAYER = false;
    public static final boolean ALWAYS_SHOW_TARGET_POINTER = false;

    // misc
    public static final int LAYERS_VIEW_BACKGROUND_COLOR = Color.BLACK; // ignored if ALLOW_CAMERA = true
    public static final int PAINT_FLAGS = Paint.ANTI_ALIAS_FLAG;
    public static final long MAX_GAME_TIME = TimeUnit.MINUTES.toMillis(30); // easter and bugs...
}
