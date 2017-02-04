package com.stedi.gyrshot.layers.menus.buttons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.constants.Styles;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

public class SimpleButton extends Layer implements LayersView.OnNewTranslateValues {
    private final Paint fillPaint = PaintFactory.create(PaintFactory.Type.BUTTON_BODY);
    private final Paint borderPaint = PaintFactory.create(PaintFactory.Type.BUTTON_BORDER);
    private final Paint textPaint = PaintFactory.create(PaintFactory.Type.BUTTON_TEXT);

    public class OnShot implements ShotCallback {
        public final int id;

        public OnShot(int id) {
            this.id = id;
        }
    }

    private FloatRect boundsRect;
    private FloatRect drawRect;

    private int id;
    private CharSequence text;

    private float xOffset, yOffset;
    private float centerX, centerY;

    public SimpleButton(int id, CharSequence text) {
        this.id = id;
        this.text = text;
        init();
    }

    public void setXYOffset(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        calculateBoundsRect();
        calculateDrawRect();
    }

    public FloatRect getBoundsRect() {
        return boundsRect;
    }

    @Override
    public void onAddToLayersView(LayersView layersView) {
        layersView.addOnNewTranslateValuesListener(this);
    }

    @Override
    public void onRemoveFromLayersView(LayersView layersView) {
        layersView.removeOnNewTranslateValuesListener(this);
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        fillPaint.setColor(drawRect.isInside(centerX, centerY) ? Color.YELLOW : Color.WHITE);
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom, fillPaint);
        canvas.drawRect(drawRect.left, drawRect.top, drawRect.right, drawRect.bottom, borderPaint);
        canvas.drawText(text, 0, text.length(), xOffset, yOffset, textPaint);
    }

    @Override
    public OnShot onShot(float shotX, float shotY) {
        if (drawRect.isInside(shotX, shotY))
            return new OnShot(id);
        return null;
    }

    @Override
    public void onGyroXYOffset(float gyroXOffset, float gyroYOffset) {
        centerX = -gyroXOffset;
        centerY = -gyroYOffset;
    }

    @Override
    public void onRotationZ(float rotationZ) {
        // TODO support for rotation vector
    }

    private void init() {
        calculateBoundsRect();
        calculateDrawRect();
    }

    private void calculateBoundsRect() {
        if (boundsRect == null)
            boundsRect = new FloatRect(Styles.Sizes.BUTTON_WIDTH, Styles.Sizes.BUTTON_HEIGHT);
        boundsRect = new FloatRect(boundsRect.left + xOffset, boundsRect.top + yOffset,
                boundsRect.right + xOffset, boundsRect.bottom + yOffset);
    }

    private void calculateDrawRect() {
        float padding = Styles.Sizes.BUTTON_PADDING;
        drawRect = new FloatRect(boundsRect.left + padding, boundsRect.top + padding,
                boundsRect.right - padding, boundsRect.bottom - padding);
    }
}
