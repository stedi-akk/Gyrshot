package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.Mode;

public class TestLayer extends Layer {
    private Paint paint;

    public TestLayer() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(App.dp2px(1));
        paint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas canvas, float xOffset, float yOffset, Mode mode) {
        canvas.drawRect(mode.rect.left + xOffset, mode.rect.top + yOffset,
                mode.rect.right + xOffset, mode.rect.bottom + yOffset, paint);
    }
}
