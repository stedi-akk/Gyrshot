package com.stedi.gyrshot.other;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.AppConfig;

public enum Mode {
    MENU(AppConfig.STATIC_MENU_SIZE[0], AppConfig.STATIC_MENU_SIZE[1]),
    GAME(AppConfig.STATIC_GAME_SIZE[0], AppConfig.STATIC_GAME_SIZE[1]);

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
