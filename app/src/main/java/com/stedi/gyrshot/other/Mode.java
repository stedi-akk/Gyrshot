package com.stedi.gyrshot.other;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.CoreConfig;

public enum Mode {
    MENU(CoreConfig.STATIC_MENU_SIZE[0], CoreConfig.STATIC_MENU_SIZE[1]),
    GAME(CoreConfig.STATIC_GAME_SIZE[0], CoreConfig.STATIC_GAME_SIZE[1]);

    private FloatRect zoneRect;

    Mode(int zoneWidthDp, int zoneHeightDp) {
        setZoneSize(App.dp2px(zoneWidthDp), App.dp2px(zoneHeightDp));
    }

    public static void overrideZoneSize(Mode mode, float zoneWidthPx, float zoneHeightPx) {
        mode.setZoneSize(zoneWidthPx, zoneHeightPx);
    }

    private void setZoneSize(float zoneWidthPx, float zoneHeightPx) {
        this.zoneRect = new FloatRect(zoneWidthPx, zoneHeightPx);
    }

    public FloatRect getZoneRect() {
        return zoneRect;
    }
}
