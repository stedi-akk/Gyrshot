package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.Games;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.other.CountUpTimer;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

import java.util.List;

public class GameInfoLayer extends Layer implements GameLayer.TargetsListener {
    private final Paint textPaint = PaintFactory.create(PaintFactory.Type.GAME_INFO_TEXT);

    private String formattedGameTime;

    private int[] timerPosition;

    private CountUpTimer countUpTimer = new CountUpTimer(Games.GAME_TIMER_FORMAT) {
        @Override
        public void onTimeUp(String formattedTime) {
            formattedGameTime = formattedTime;
        }

        @Override
        public void onFinish() {
            // TODO easter
        }
    };

    @Override
    public void onRemoveFromLayersView(LayersView layersView) {
        countUpTimer.stopCountUp();
    }

    @Override
    public void onPause() {
        countUpTimer.stopCountUp();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (!countUpTimer.isActive()) {
            formattedGameTime = countUpTimer.startCountUp();
        }

        if (timerPosition == null) {
            int textHeight = App.getTextHeight(formattedGameTime, textPaint);
            timerPosition = new int[]{0, textHeight - canvas.getHeight() / 2};
        }

        canvas.drawText(formattedGameTime, timerPosition[0], timerPosition[1], textPaint);
    }

    @Override
    public void onNewTarget(Target target) {

    }

    @Override
    public void onDrawTargets(List<Target> targets) {

    }

    @Override
    public void onTargetDelete(Target target, boolean fromShot) {

    }
}
