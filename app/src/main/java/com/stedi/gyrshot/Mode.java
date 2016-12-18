package com.stedi.gyrshot;

import android.graphics.Rect;

public enum Mode {
    MENU(Config.STATIC_MENU_SIZE[0], Config.STATIC_MENU_SIZE[1]),
    GAME(Config.STATIC_GAME_SIZE[0], Config.STATIC_GAME_SIZE[1]);

    private Rect zoneRect;

    Mode(int zoneWidthDp, int zoneHeightDp) {
        setZoneSize((int) App.dp2px(zoneWidthDp), (int) App.dp2px(zoneHeightDp));
    }

    public static void overrideZoneSize(Mode mode, int zoneWidthPx, int zoneHeightPx) {
        mode.setZoneSize(zoneWidthPx, zoneHeightPx);
    }

    private void setZoneSize(int zoneWidthPx, int zoneHeightPx) {
        this.zoneRect = new Rect(-zoneWidthPx / 2, -zoneHeightPx / 2, zoneWidthPx / 2, zoneHeightPx / 2);
    }

    public Rect getZoneRect() {
        return zoneRect;
    }
}
