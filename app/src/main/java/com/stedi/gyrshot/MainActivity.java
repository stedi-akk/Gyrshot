package com.stedi.gyrshot;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        hideNavbar();
    }

    private void hideNavbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
