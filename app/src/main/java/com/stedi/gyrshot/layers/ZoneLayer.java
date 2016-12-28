package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class ZoneLayer extends Layer {
    private Paint paint;

    public ZoneLayer() {
        paint = PaintFactory.create(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(App.dp2px(2));
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        canvas.drawRect(zoneRect.left, zoneRect.top, zoneRect.right, zoneRect.bottom, paint);
        return true;
    }
}
