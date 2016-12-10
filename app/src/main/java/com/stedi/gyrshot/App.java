package com.stedi.gyrshot;

import android.app.Application;
import android.util.TypedValue;

public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, instance.getResources().getDisplayMetrics());
    }
}
