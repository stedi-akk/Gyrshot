package com.stedi.gyrshot;

import android.graphics.Rect;

public enum Mode {
    MENU(200, 200),
    GAME(1000, 1000);

    public final int zoneWidthPx;
    public final int zoneHeightPx;
    public final Rect rect;

    Mode(int zoneWidthDp, int zoneHeightDp) {
        this.zoneWidthPx = (int) App.dp2px(zoneWidthDp);
        this.zoneHeightPx = (int) App.dp2px(zoneHeightDp);
        this.rect = new Rect(-zoneWidthPx / 2, -zoneHeightPx / 2, zoneWidthPx / 2, zoneHeightPx / 2);
    }
}
