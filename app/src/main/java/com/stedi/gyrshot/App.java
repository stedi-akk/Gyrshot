package com.stedi.gyrshot;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;

import com.stedi.gyrshot.constants.CoreConfig;
import com.stedi.gyrshot.other.Mode;

import java.util.Random;

public class App extends Application {
    private static App instance;

    private Random random;

    private boolean inOnResume;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        random = new Random();

        if (CoreConfig.MODE_ZONE_SIZE_DEPENDS_ON_SCREEN)
            initModeZoneSizeByScreen();
    }

    private void initModeZoneSizeByScreen() {
        int zoneSizeByScreen = Math.max(getResources().getDisplayMetrics().heightPixels,
                getResources().getDisplayMetrics().widthPixels);

        Mode.overrideZoneSize(Mode.MENU,
                zoneSizeByScreen * CoreConfig.MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN[0],
                zoneSizeByScreen * CoreConfig.MENU_ZONE_SIZE_MULTIPLIER_BY_SCREEN[1]);

        Mode.overrideZoneSize(Mode.GAME,
                zoneSizeByScreen * CoreConfig.GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN[0],
                zoneSizeByScreen * CoreConfig.GAME_ZONE_SIZE_MULTIPLIER_BY_SCREEN[1]);
    }

    public static void onResume() {
        instance.inOnResume = true;
    }

    public static void onPause() {
        instance.inOnResume = false;
    }

    public static boolean inOnResume() {
        return instance.inOnResume;
    }

    public static Resources getRes() {
        return instance.getResources();
    }

    public static float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, instance.getResources().getDisplayMetrics());
    }

    public static float rand(float from, float to) {
        return rand((int) from, (int) to);
    }

    public static int rand(int from, int to) {
        int n = (to - from) + 1;
        return n < 0 ? 0 : instance.random.nextInt(n) + from;
    }

    public static int getTextHeight(String text, Paint textPaint) {
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.height();
    }

    public static void log(Object classObj, String message) {
        if (BuildConfig.DEBUG)
            Log.d("Gyrshot", classObj.getClass().getSimpleName() + ": " + message);
    }
}
