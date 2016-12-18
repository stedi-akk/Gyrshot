package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.stedi.gyrshot.App;

public class ZoneLayer extends Layer {
    private Paint paint;

    public ZoneLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(App.dp2px(1));
        paint.setColor(Color.GREEN);
    }

    @Override
    public boolean onDraw(Canvas canvas, Rect zoneRect, Rect actualRect) {
        canvas.drawRect(zoneRect.left, zoneRect.top, zoneRect.right, zoneRect.bottom, paint);
        return true;
    }
}
