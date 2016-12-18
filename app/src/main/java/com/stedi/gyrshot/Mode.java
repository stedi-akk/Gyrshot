package com.stedi.gyrshot;

import com.stedi.gyrshot.other.FloatRect;

public enum Mode {
    MENU(Config.STATIC_MENU_SIZE[0], Config.STATIC_MENU_SIZE[1]),
    GAME(Config.STATIC_GAME_SIZE[0], Config.STATIC_GAME_SIZE[1]);

    private FloatRect zoneRect;

    Mode(int zoneWidthDp, int zoneHeightDp) {
        setZoneSize(App.dp2px(zoneWidthDp), App.dp2px(zoneHeightDp));
    }

    public static void overrideZoneSize(Mode mode, float zoneWidthPx, float zoneHeightPx) {
        mode.setZoneSize(zoneWidthPx, zoneHeightPx);
    }

    private void setZoneSize(float zoneWidthPx, float zoneHeightPx) {
        this.zoneRect = new FloatRect(-zoneWidthPx / 2, -zoneHeightPx / 2, zoneWidthPx / 2, zoneHeightPx / 2);
    }

    public FloatRect getZoneRect() {
        return zoneRect;
    }
}
