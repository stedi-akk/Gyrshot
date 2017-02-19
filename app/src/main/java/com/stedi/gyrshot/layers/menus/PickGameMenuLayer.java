package com.stedi.gyrshot.layers.menus;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.views.ButtonLayer;

import java.util.ArrayList;
import java.util.List;

public class PickGameMenuLayer extends SimpleMenuLayer {
    private final List<ButtonLayer> buttons = new ArrayList<>();

    public class OnShot implements ShotCallback {
        public final Games.Type type;

        public OnShot(Games.Type type) {
            this.type = type;
        }
    }

    public PickGameMenuLayer() {
        for (Games.Type type : Games.Type.values()) {
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
                return new PickGameMenuLayer.OnShot(Games.Type.find(callback.id));
        }
        return null;
    }
}
