package com.stedi.gyrshot.layers.menus;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.layers.ShotCallback;
import com.stedi.gyrshot.layers.menus.buttons.SimpleButton;

import java.util.ArrayList;
import java.util.List;

public class PickGameMenuLayer extends SimpleMenuLayer {
    private final List<SimpleButton> buttons = new ArrayList<>();

    public class OnShot implements ShotCallback {
        public final Games.Type type;

        public OnShot(Games.Type type) {
            this.type = type;
        }
    }

    public PickGameMenuLayer() {
        for (Games.Type type : Games.Type.values()) {
            SimpleButton btn = new SimpleButton(type.id, App.getRes().getText(type.resTitle));
            buttons.add(btn);
        }
        prepare();
    }

    @Override
    public SimpleButton[] getButtons() {
        return buttons.toArray(new SimpleButton[buttons.size()]);
    }

    @Override
    public ShotCallback onShot(float shotX, float shotY) {
        for (SimpleButton button : buttons) {
            SimpleButton.OnShot callback = button.onShot(shotX, shotY);
            if (callback != null)
                return new PickGameMenuLayer.OnShot(Games.Type.find(callback.id));
        }
        return null;
    }
}
