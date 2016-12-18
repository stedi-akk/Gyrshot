package com.stedi.gyrshot;

import android.app.Application;
import android.util.Log;
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

        if (Config.MODE_ZONE_SIZE_DEPENDS_ON_SCREEN)
            initModeZoneSizeByScreen();
    }

    private void initModeZoneSizeByScreen() {
        int zoneSizeByScreen = Math.max(getResources().getDisplayMetrics().heightPixels,
                getResources().getDisplayMetrics().widthPixels);

        Mode.overrideZoneSize(Mode.MENU,
                (int) (zoneSizeByScreen * Config.MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN[0]),
                (int) (zoneSizeByScreen * Config.MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN[1]));

        Mode.overrideZoneSize(Mode.GAME,
                (int) (zoneSizeByScreen * Config.GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN[0]),
                (int) (zoneSizeByScreen * Config.GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN[1]));
    }

    public static float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, instance.getResources().getDisplayMetrics());
    }

    public static int rand(int from, int to) {
        int n = (to - from) + 1;
        return n < 0 ? 0 : instance.random.nextInt(n) + from;
    }

    public static void log(Object classObj, String message) {
        if (BuildConfig.DEBUG)
            Log.d("Gyrshot", classObj.getClass().getSimpleName() + ": " + message);
    }
}
