package com.stedi.gyrshot.layers.gamelayers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.GamesConfig;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.layers.views.TextLayer;
import com.stedi.gyrshot.other.CountUpTimer;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

import java.util.List;

public class GameInfoLayer extends Layer implements GameLayer.TargetsListener {
    private final Paint textPaint = PaintFactory.create(PaintFactory.Type.GAME_INFO_TEXT);

    private final TextLayer timerText = new TextLayer(textPaint);
    private final TextLayer deletedSelfText = new TextLayer(textPaint);
    private final TextLayer deletedFromShotText = new TextLayer(textPaint);

    private final float layerPadding = App.dp2px(15);

    private boolean inPause;

    private CountUpTimer countUpTimer;

    private String formattedTime;
    private int deletedSelf;
    private int deletedFromShot;

    @Override
    public void onAddToLayersView(LayersView layersView) {
        countUpTimer = new CountUpTimer(GamesConfig.GAME_TIMER_FORMAT) {
            @Override
            public void onTimeUp(String formattedTime) {
                GameInfoLayer.this.formattedTime = formattedTime;
            }

            @Override
            public void onFinish() {
                // TODO easter
            }
        };
    }

    @Override
    public void onRemoveFromLayersView(LayersView layersView) {
        countUpTimer.stopCountUp();
        countUpTimer = null;
    }

    @Override
    public void onResume() {
        inPause = false;
    }

    @Override
    public void onPause() {
        inPause = true;
        countUpTimer.stopCountUp();
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        drawTimer(canvas, zoneRect, actualRect);
        drawTargetsInfo(canvas, zoneRect, actualRect);
    }

    private void drawTimer(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (countUpTimer != null && !inPause && !countUpTimer.isActive())
            formattedTime = countUpTimer.startCountUp();

        if (formattedTime == null)
            return;

        if (timerText.getTextPosition() == null)
            timerText.setPosition(0, -canvas.getHeight() / 2 + layerPadding);

        timerText.setText(formattedTime);
        timerText.onDraw(canvas, zoneRect, actualRect);
    }

    private void drawTargetsInfo(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (deletedFromShotText.getTextPosition() == null)
            deletedFromShotText.setPosition(canvas.getWidth() / 3, -canvas.getHeight() / 2 + layerPadding);

        if (deletedSelfText.getTextPosition() == null)
            deletedSelfText.setPosition(-canvas.getWidth() / 3, -canvas.getHeight() / 2 + layerPadding);

        deletedFromShotText.setText(String.valueOf(deletedFromShot));
        deletedFromShotText.onDraw(canvas, zoneRect, actualRect);

        deletedSelfText.setText(String.valueOf(deletedSelf));
        deletedSelfText.onDraw(canvas, zoneRect, actualRect);
    }

    @Override
    public void onNewTarget(Target target) {

    }

    @Override
    public void onDrawTargets(List<Target> targets) {

    }

    @Override
    public void onTargetDelete(Target target, boolean fromShot) {
        if (fromShot)
            deletedFromShot++;
        else
            deletedSelf++;
    }
}
