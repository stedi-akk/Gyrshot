package com.stedi.gyrshot.layers.menus;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.R;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.views.ButtonLayer;

import java.util.ArrayList;
import java.util.List;

public class StartMenuLayer extends SimpleMenuLayer {
    private final List<ButtonLayer> buttons = new ArrayList<>();

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
        for (Type type : Type.values()) {
            ButtonLayer btn = new ButtonLayer(type.id, App.getRes().getText(type.resTitle).toString());
            buttons.add(btn);
        }
        prepare();
    }

    @Override
    public ButtonLayer[] getButtons() {
        return buttons.toArray(new ButtonLayer[buttons.size()]);
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        for (ButtonLayer button : buttons) {
            ButtonLayer.OnShot callback = button.onShot(shotX, shotY);
            if (callback != null)
                return new OnShot(Type.find(callback.id));
        }
        return null;
    }
}
