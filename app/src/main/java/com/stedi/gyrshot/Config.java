package com.stedi.gyrshot;

import android.graphics.Color;

public final class Config {
    public static final float GYROSCOPE_ACCURACY = 1.5f;
    public static final boolean ALLOW_CAMERA = true;
    public static final boolean SHOW_DEBUG_LAYER = true;
    public static final int LAYERS_VIEW_FPS = 30;
    public static final int LAYERS_VIEW_BACKGROUND_COLOR = Color.BLACK;
    public static final boolean MODE_ZONE_SIZE_DEPENDS_ON_SCREEN = true;
    public static final float[] MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{2.6f, 1.8f};
    public static final float[] GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN = new float[]{3f, 3f};
    public static final boolean ATTACH_ZONE_RECT_TO_SCREEN_EDGES = true;
    public static final int[] STATIC_MENU_SIZE = new int[]{500, 500};
    public static final int[] STATIC_GAME_SIZE = new int[]{1000, 1000};
}
