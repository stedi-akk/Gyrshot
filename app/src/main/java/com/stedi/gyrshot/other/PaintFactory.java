package com.stedi.gyrshot.other;

import android.graphics.Paint;

import com.stedi.gyrshot.config.Config;
import com.stedi.gyrshot.config.Styles;

public class PaintFactory {
    public enum Type {
        BUTTON_BODY,
        BUTTON_TEXT,
        BUTTON_BORDER,
    }

    public static Paint create() {
        return new Paint(Config.PAINT_FLAGS);
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
        }

        return paint;
    }
}
