package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.stedi.gyrshot.App;

public class DebugLayer extends Layer {
    private Paint debugTextPaint;
    private Paint offsetRectPaint;
    private Paint lastShotPaint;

    private String debugText;
    private boolean showOffsetRect;
    private boolean showZoneRect;
    private Float lastShotX, lastShotY;

    private Paint getDebugTextPaint() {
        if (debugTextPaint == null) {
            debugTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugTextPaint.setTextSize(App.dp2px(15));
            debugTextPaint.setColor(Color.WHITE);
        }
        return debugTextPaint;
    }

    private Paint getRectPaint() {
        if (offsetRectPaint == null) {
            offsetRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            offsetRectPaint.setStyle(Paint.Style.STROKE);
            offsetRectPaint.setStrokeWidth(1f);
            offsetRectPaint.setColor(Color.RED);
        }
        return offsetRectPaint;
    }

    private Paint getLastShotPaint() {
        if (lastShotPaint == null) {
            lastShotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            lastShotPaint.setColor(Color.YELLOW);
        }
        return lastShotPaint;
    }

    public void showDebugText(String debugText) {
        this.debugText = debugText;
    }

    public void showOffsetRect(boolean showOffsetRect) {
        this.showOffsetRect = showOffsetRect;
    }

    public void showZoneRect(boolean showZoneRect) {
        this.showZoneRect = showZoneRect;
    }

    public void showLastShot(float lastShotX, float lastShotY) {
        this.lastShotX = lastShotX;
        this.lastShotY = lastShotY;
    }

    @Override
    public boolean onDraw(Canvas canvas, Rect zoneRect, Rect offsetRect) {
        if (debugText != null) {
            Paint paint = getDebugTextPaint();
            paint.setColor(Color.BLACK);
            canvas.drawText(debugText, paint.getTextSize() + 1, paint.getTextSize() * 2 + 1, paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawText(debugText, paint.getTextSize(), paint.getTextSize() * 2, paint);
        }

        if (showOffsetRect) {
            Paint paint = getRectPaint();
            canvas.drawRect(offsetRect.left, offsetRect.top, offsetRect.right, offsetRect.bottom, paint);
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
