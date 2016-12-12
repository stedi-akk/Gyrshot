package com.stedi.gyrshot;

import android.app.Application;
import android.util.TypedValue;

import java.util.Random;

public class App extends Application {
    private static App instance;

    private Random random;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        random = new Random();

        if (Config.MODE_ZONE_SIZE_BY_SCREEN)
            initModeZoneSizeByScreen();
    }

    private void initModeZoneSizeByScreen() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int menuZoneSize = (int) (screenWidth * 1.5);
        int gameZoneSize = screenWidth * 2;
        Mode.overrideZoneSize(Mode.MENU, menuZoneSize, menuZoneSize);
        Mode.overrideZoneSize(Mode.GAME, gameZoneSize, gameZoneSize);
    }

    public static float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, instance.getResources().getDisplayMetrics());
    }

    public static int rand(int from, int to) {
        return instance.random.nextInt((to - from) + 1) + from;
    }
}
