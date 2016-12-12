package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Mode;

public class ZoneLayer extends Layer {
    private Paint paint;

    public ZoneLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(App.dp2px(1));
        paint.setColor(Color.GREEN);
    }

    @Override
    public boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        Rect rect = mode.getZoneRect();
        canvas.drawRect(rect.left + xOffset, rect.top + yOffset,
                rect.right + xOffset, rect.bottom + yOffset, paint);
        return true;
    }
}
