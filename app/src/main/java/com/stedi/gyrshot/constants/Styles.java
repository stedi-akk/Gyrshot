package com.stedi.gyrshot.constants;

import android.graphics.Color;

import com.stedi.gyrshot.App;

/**
 * Styles for layers ui components (buttons, texts, etc)
 */
public final class Styles {
    public static class Colors {
        public static final int MAIN_COLOR = Color.RED;
        public static final int BUTTON_BODY = Color.WHITE;
        public static final int BUTTON_BORDER = Color.BLACK;
        public static final int BUTTON_TEXT = Color.BLACK;
    }

    public static class Sizes {
        public static final float BUTTON_HEIGHT = App.dp2px(60);
        public static final float BUTTON_WIDTH = App.dp2px(300);
        public static final float BUTTON_TEXT_SIZE = BUTTON_HEIGHT / 2;
        public static final float BUTTON_BORDER_WIDTH = 1f;
        public static final float BUTTON_PADDING = App.dp2px(2);
    }
}
