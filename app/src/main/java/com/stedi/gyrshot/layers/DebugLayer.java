package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Mode;

public class DebugLayer extends Layer {
    private Paint debugTextPaint;
    private Paint offsetRectPaint;
    private Paint lastShotPaint;

    private String debugText;
    private Rect offsetRect;

    private Float lastShotX, lastShotY;

    private Paint getDebugTextPaint() {
        if (debugTextPaint == null) {
            debugTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugTextPaint.setTextSize(App.dp2px(15));
            debugTextPaint.setColor(Color.WHITE);
        }
        return debugTextPaint;
    }

    private Paint getOffsetRectPaint() {
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

    public void showOffsetRect(Rect offsetRect) {
        this.offsetRect = offsetRect;
    }

    public void showLastShot(float lastShotX, float lastShotY) {
        this.lastShotX = lastShotX;
        this.lastShotY = lastShotY;
    }

    @Override
    public boolean onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        if (debugText != null) {
            Paint paint = getDebugTextPaint();
            paint.setColor(Color.BLACK);
            canvas.drawText(debugText, paint.getTextSize() + 1, paint.getTextSize() * 2 + 1, paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawText(debugText, paint.getTextSize(), paint.getTextSize() * 2, paint);
        }

        if (offsetRect != null) {
            Paint paint = getOffsetRectPaint();
            canvas.drawRect(offsetRect.left + xOffset, offsetRect.top + yOffset,
                    offsetRect.right + xOffset, offsetRect.bottom + yOffset, paint);
        }

        if (lastShotX != null && lastShotY != null) {
            Paint paint = getLastShotPaint();
            canvas.drawCircle(lastShotX + xOffset, lastShotY + yOffset, 10, paint);
        }

        return true;
    }
}
