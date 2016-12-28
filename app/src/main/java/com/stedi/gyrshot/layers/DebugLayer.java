package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class DebugLayer extends Layer {
    private Paint debugTextPaint;
    private Paint actualRectPaint;
    private Paint lastShotPaint;

    private String debugText;
    private boolean showActualRect;
    private boolean showZoneRect;
    private Float lastShotX, lastShotY;

    private Paint getDebugTextPaint() {
        if (debugTextPaint == null) {
            debugTextPaint = PaintFactory.create(Color.WHITE);
            debugTextPaint.setTextSize(App.dp2px(15));
        }
        return debugTextPaint;
    }

    private Paint getRectPaint() {
        if (actualRectPaint == null) {
            actualRectPaint = PaintFactory.create(Color.RED);
            actualRectPaint.setStyle(Paint.Style.STROKE);
            actualRectPaint.setStrokeWidth(1f);
        }
        return actualRectPaint;
    }

    private Paint getLastShotPaint() {
        if (lastShotPaint == null) {
            lastShotPaint = PaintFactory.create(Color.YELLOW);
        }
        return lastShotPaint;
    }

    public void showDebugText(String debugText) {
        this.debugText = debugText;
    }

    public void showActualRect(boolean showActualRect) {
        this.showActualRect = showActualRect;
    }

    public void showZoneRect(boolean showZoneRect) {
        this.showZoneRect = showZoneRect;
    }

    public void showLastShot(float lastShotX, float lastShotY) {
        this.lastShotX = lastShotX;
        this.lastShotY = lastShotY;
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (debugText != null) {
            Paint paint = getDebugTextPaint();
            paint.setColor(Color.BLACK);
            canvas.drawText(debugText, paint.getTextSize() + 1, paint.getTextSize() * 2 + 1, paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawText(debugText, paint.getTextSize(), paint.getTextSize() * 2, paint);
        }

        if (showActualRect) {
            Paint paint = getRectPaint();
            canvas.drawRect(actualRect.left, actualRect.top, actualRect.right, actualRect.bottom, paint);
        }

        if (showZoneRect) {
            Paint paint = getRectPaint();
            canvas.drawLine(zoneRect.left, zoneRect.top, zoneRect.right, zoneRect.bottom, paint);
            canvas.drawLine(zoneRect.right, zoneRect.top, zoneRect.left, zoneRect.bottom, paint);
        }

        if (lastShotX != null && lastShotY != null) {
            Paint paint = getLastShotPaint();
            canvas.drawCircle(lastShotX, lastShotY, 10, paint);
        }

        return true;
    }
}
