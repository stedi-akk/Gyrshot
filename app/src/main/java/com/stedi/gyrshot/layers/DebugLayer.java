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

    private StringBuilder debugText;
    private float debugTextXOffset, debugTextYOffset;
    private boolean showActualRect;
    private boolean showZoneRect;
    private Float lastShotX, lastShotY;

    private Paint getDebugTextPaint() {
        if (debugTextPaint == null) {
            debugTextPaint = PaintFactory.create(Color.WHITE);
            debugTextPaint.setTextSize(App.dp2px(18));
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

    public void prepareDebugText(float gyroXOffset, float gyroYOffset) {
        if (debugText == null)
            debugText = new StringBuilder();
        this.debugTextXOffset = gyroXOffset;
        this.debugTextYOffset = gyroYOffset;
    }

    public void addDebugText(String debugText) {
        if (debugText != null)
            this.debugText.append(debugText).append("\n");
    }

    public void clearDebugText() {
        if (debugText != null)
            debugText.setLength(0);
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
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (debugText != null) {
            canvas.save();
            canvas.translate(-debugTextXOffset, -debugTextYOffset); // ignoring offset from gyroscope
            canvas.translate(-canvas.getWidth() / 2, -canvas.getHeight() / 2); // left-top position
            Paint paint = getDebugTextPaint();
            int i = 0;
            for (String line : debugText.toString().split("\n")) {
                paint.setColor(Color.BLACK);
                canvas.drawText(line, paint.getTextSize() + 1,
                        paint.getTextSize() * i + paint.getTextSize() * 2 + 1, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(line, paint.getTextSize(),
                        paint.getTextSize() * i + paint.getTextSize() * 2, paint);
                i++;
            }
            canvas.restore();
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
    }
}
