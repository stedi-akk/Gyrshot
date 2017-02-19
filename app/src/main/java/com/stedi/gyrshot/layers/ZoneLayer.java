package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.CoreConfig;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class ZoneLayer extends Layer {
    private Paint paint;
    private Paint debugPaint;

    public ZoneLayer() {
        paint = PaintFactory.create(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(App.dp2px(2));
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        canvas.drawRect(zoneRect.left, zoneRect.top, zoneRect.right, zoneRect.bottom, paint);

        if (CoreConfig.DEBUG_ZONE_LAYER)
            drawDebug(canvas, zoneRect, actualRect);
    }

    private void drawDebug(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        Paint paint = getDebugPaint();
        canvas.drawRect(actualRect.left, actualRect.top, actualRect.right, actualRect.bottom, paint);
        canvas.drawLine(zoneRect.left, zoneRect.top, zoneRect.right, zoneRect.bottom, paint);
        canvas.drawLine(zoneRect.right, zoneRect.top, zoneRect.left, zoneRect.bottom, paint);
    }

    private Paint getDebugPaint() {
        if (debugPaint == null) {
            debugPaint = PaintFactory.create(Color.RED);
            debugPaint.setStyle(Paint.Style.STROKE);
            debugPaint.setStrokeWidth(1f);
        }
        return debugPaint;
    }
}
