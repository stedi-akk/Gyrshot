package com.stedi.gyrshot.constants;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Application control params
 */
public final class AppConfig {
    // core
    public static final float GYROSCOPE_ACCURACY = 1.5f;
    public static final boolean ALLOW_CAMERA = true;
    public static final int LAYERS_VIEW_MAX_FPS = 60;

    // zone size
    public static final boolean MODE_ZONE_SIZE_DEPENDS_ON_SCREEN = true;
    public static final boolean ATTACH_ZONE_RECT_TO_SCREEN_EDGES = true;
    public static final float[] MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{2.6f, 1.8f};
    public static final float[] GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{3f, 3f};
    public static final int[] STATIC_MENU_SIZE = new int[]{500, 500};
    public static final int[] STATIC_GAME_SIZE = new int[]{1000, 1000};

    // debug info
    public static final boolean SHOW_DEBUG_LAYER = true;
    public static final boolean DEBUG_LAYER_SHOW_FPS = true;
    public static final boolean DEBUG_LAYER_SHOW_ZONE_RECT = true;
    public static final boolean DEBUG_LAYER_SHOW_ACTUAL_RECT = true;
    public static final boolean DEBUG_LAYER_SHOW_LAST_SHOT = true;

    // misc
    public static final int LAYERS_VIEW_BACKGROUND_COLOR = Color.BLACK;
    public static final int PAINT_FLAGS = Paint.ANTI_ALIAS_FLAG;
}