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
    }

    public static float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, instance.getResources().getDisplayMetrics());
    }

    public static int rand(int from, int to) {
        return instance.random.nextInt((to - from) + 1) + from;
    }
}
