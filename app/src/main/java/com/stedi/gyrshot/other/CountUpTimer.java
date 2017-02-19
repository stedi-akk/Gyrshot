package com.stedi.gyrshot.other;

import android.os.CountDownTimer;

import com.stedi.gyrshot.constants.CoreConfig;

import java.util.concurrent.TimeUnit;

public abstract class CountUpTimer extends CountDownTimer {
    private long seconds;

    private String format;

    private boolean isActive;

    public CountUpTimer(String format) {
        super(System.currentTimeMillis() + CoreConfig.MAX_GAME_TIME, TimeUnit.SECONDS.toMillis(1));
        this.format = format;
    }

    public abstract void onTimeUp(String formattedTime);

    public String startCountUp() {
        isActive = true;
        String lastTime = formatSeconds(seconds);
        start();
        return lastTime;
    }

    public void stopCountUp() {
        cancel();
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        seconds++;
        onTimeUp(formatSeconds(seconds));
    }

    private String formatSeconds(long seconds) {
        return String.format(format, (seconds % 3600) / 60, seconds % 60);
    }
}
