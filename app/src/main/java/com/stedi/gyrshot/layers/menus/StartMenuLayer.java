package com.stedi.gyrshot.layers.menus;

import android.graphics.Canvas;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.R;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.menus.buttons.SimpleButton;
import com.stedi.gyrshot.other.FloatRect;

import java.util.ArrayList;
import java.util.List;

public class StartMenuLayer extends Layer implements SimpleButton.ShotCallback {
    private final List<SimpleButton> buttons = new ArrayList<>();

    public enum Type {
        START_GAME(1001, R.string.start_game),
        EXIT(1002, R.string.exit);

        private final int id;
        private final int resTitle;

        Type(int id, int resString) {
            this.id = id;
            this.resTitle = resString;
        }
    }

    public StartMenuLayer() {
        float buttonsHeight = 0;

        for (Type type : Type.values()) {
            SimpleButton btn = new SimpleButton(type.id, App.getRes().getText(type.resTitle), this);
            buttons.add(btn);
            buttonsHeight += btn.getBoundsRect().getHeight();
        }

        for (int i = 0; i < buttons.size(); i++) {
            SimpleButton btn = buttons.get(i);
            float btnHeight = btn.getBoundsRect().getHeight();
            btn.setXYOffset(0, -buttonsHeight / 2 + btnHeight / 2 + i * btnHeight);
        }
    }

    @Override
    public boolean onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        for (SimpleButton button : buttons)
            button.onDraw(canvas, zoneRect, actualRect);

        return true;
    }

    @Override
    public void onButtonShot(int id) {

    }

    @Override
    public boolean onShot(float shotX, float shotY) {
        return super.onShot(shotX, shotY);
    }
}
