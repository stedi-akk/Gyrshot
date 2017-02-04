package com.stedi.gyrshot.layers.menus;

import android.graphics.Canvas;

import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.menus.buttons.SimpleButton;
import com.stedi.gyrshot.other.FloatRect;

public abstract class SimpleMenuLayer extends Layer {
    private SimpleButton[] buttons;

    public abstract SimpleButton[] getButtons();

    @Override
    public void onAddToLayersView(LayersView layersView) {
        if (buttons == null)
            throw new IllegalArgumentException("prepare() was not called");

        for (SimpleButton button : buttons)
            button.onAddToLayersView(layersView);
    }

    @Override
    public void onRemoveFromLayersView(LayersView layersView) {
        for (SimpleButton button : buttons)
            button.onRemoveFromLayersView(layersView);
    }

    protected void prepare() {
        if (buttons != null)
            throw new IllegalArgumentException("prepare() should be called only once");

        buttons = getButtons();

        float buttonsHeight = 0;
        for (SimpleButton btn : buttons)
            buttonsHeight += btn.getBoundsRect().getHeight();

        for (int i = 0; i < buttons.length; i++) {
            SimpleButton btn = buttons[i];
            float btnHeight = btn.getBoundsRect().getHeight();
            btn.setXYOffset(0, -buttonsHeight / 2 + btnHeight / 2 + i * btnHeight);
        }
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        for (SimpleButton button : buttons)
            button.onDraw(canvas, zoneRect, actualRect);
    }
}
