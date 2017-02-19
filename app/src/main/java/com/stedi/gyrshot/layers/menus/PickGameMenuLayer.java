package com.stedi.gyrshot.layers.menus;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.GamesConfig;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.views.ButtonLayer;

import java.util.ArrayList;
import java.util.List;

public class PickGameMenuLayer extends SimpleMenuLayer {
    private final List<ButtonLayer> buttons = new ArrayList<>();

    public class OnShot implements ShotCallback {
        public final GamesConfig.Type type;

        public OnShot(GamesConfig.Type type) {
            this.type = type;
        }
    }

    public PickGameMenuLayer() {
        for (GamesConfig.Type type : GamesConfig.Type.values()) {
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
                return new PickGameMenuLayer.OnShot(GamesConfig.Type.find(callback.id));
        }
        return null;
    }
}
