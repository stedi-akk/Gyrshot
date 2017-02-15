package com.stedi.gyrshot.other;

import android.graphics.Paint;

import com.stedi.gyrshot.constants.AppConfig;
import com.stedi.gyrshot.constants.Styles;

public class PaintFactory {
    public enum Type {
        BUTTON_BODY,
        BUTTON_TEXT,
        BUTTON_BORDER,
        GAME_INFO_TEXT,
    }

    public static Paint create() {
        return new Paint(AppConfig.PAINT_FLAGS);
    }

    public static Paint create(int color) {
        Paint paint = create();
        paint.setColor(color);
        return paint;
    }

    public static Paint create(Type type) {
        Paint paint = create();

        switch (type) {
            case BUTTON_BODY:
                paint.setColor(Styles.Colors.BUTTON_BODY);
                break;
            case BUTTON_TEXT:
                paint.setTextSize(Styles.Sizes.BUTTON_TEXT_SIZE);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Styles.Colors.BUTTON_TEXT);
                break;
            case BUTTON_BORDER:
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Styles.Colors.BUTTON_BORDER);
                paint.setStrokeWidth(Styles.Sizes.BUTTON_BORDER_WIDTH);
                break;
            case GAME_INFO_TEXT:
                paint.setTextSize(Styles.Sizes.GAME_INFO_TEXT_SIZE);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Styles.Colors.GAME_INFO_TEXT);
                break;
        }

        return paint;
    }
}
