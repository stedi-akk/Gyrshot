package com.stedi.gyrshot.layers.menus;

import android.graphics.Canvas;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.R;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.menus.buttons.SimpleButton;
import com.stedi.gyrshot.other.FloatRect;

import java.util.ArrayList;
import java.util.List;

public class StartMenuLayer extends Layer {
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

        public static Type find(int id) {
            for (Type type : Type.values()) {
                if (type.id == id)
                    return type;
            }
            return null;
        }
    }

    public class OnShot implements ShotCallback {
        public final Type type;

        public OnShot(Type type) {
            this.type = type;
        }
    }

    public StartMenuLayer() {
        float buttonsHeight = 0;

        for (Type type : Type.values()) {
            SimpleButton btn = new SimpleButton(type.id, App.getRes().getText(type.resTitle));
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
    public ShotCallback onShot(float shotX, float shotY) {
        for (SimpleButton button : buttons) {
            SimpleButton.OnShot callback = button.onShot(shotX, shotY);
            if (callback != null)
                return new OnShot(Type.find(callback.id));
        }
        return null;
    }
}
