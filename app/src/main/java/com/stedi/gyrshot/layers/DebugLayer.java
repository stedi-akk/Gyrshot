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

    private String debugText;
    private Rect offsetRect;
    private float centerX, centerY;

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

    public void showDebugText(String debugText) {
        this.debugText = debugText;
    }

    public void showOffsetRect(Rect offsetRect, float centreX, float centerY) {
        this.offsetRect = offsetRect;
        this.centerX = centreX;
        this.centerY = centerY;
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
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.drawRect(offsetRect.left + xOffset, offsetRect.top + yOffset,
                    offsetRect.right + xOffset, offsetRect.bottom + yOffset, paint);
            canvas.restore();
        }

        return true;
    }
}
