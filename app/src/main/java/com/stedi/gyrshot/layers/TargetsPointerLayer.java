package com.stedi.gyrshot.layers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

import java.util.List;

public class TargetsPointerLayer extends Layer implements GameLayer.TargetsListener, LayersView.OnSensorValues {
    private final Paint paint = PaintFactory.create(Color.BLUE);

    private List<Target> gameTargets;

    private float gyroXOffset, gyroYOffset;

    private FloatRect drawRect;

    public TargetsPointerLayer() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas, FloatRect zoneRect, FloatRect actualRect) {
        if (gameTargets != null) {
            if (drawRect == null) {
                drawRect = new FloatRect(canvas.getWidth(), canvas.getHeight());
            }

            for (Target target : gameTargets) {

                float realX = target.getX() + gyroXOffset;
                float realY = target.getY() + gyroYOffset;

                realX = drawRect.forceInLeftRight(realX);
                realY = drawRect.forceInTopBottom(realY);

                canvas.drawCircle(realX - target.getRadius(), realY, 10, paint);
                canvas.drawCircle(realX, realY - target.getRadius(), 10, paint);
                canvas.drawCircle(realX + target.getRadius(), realY, 10, paint);
                canvas.drawCircle(realX, realY + target.getRadius(), 10, paint);
            }
        }
    }

    @Override
    public void onNewTarget(Target target) {

    }

    @Override
    public void onDrawTargets(List<Target> targets) {
        gameTargets = targets;
    }

    @Override
    public void onTargetDelete(Target target) {

    }

    @Override
    public void onGyroXYOffset(float gyroXOffset, float gyroYOffset) {
        this.gyroXOffset = gyroXOffset;
        this.gyroYOffset = gyroYOffset;
    }

    @Override
    public void onRotationZ(float rotationZ) {
        // TODO react from rotation vector
    }
}
