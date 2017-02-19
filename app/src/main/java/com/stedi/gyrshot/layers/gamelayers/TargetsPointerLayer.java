package com.stedi.gyrshot.layers.gamelayers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.stedi.gyrshot.constants.CoreConfig;
import com.stedi.gyrshot.layers.Layer;
import com.stedi.gyrshot.layers.LayersView;
import com.stedi.gyrshot.layers.targets.Target;
import com.stedi.gyrshot.other.FloatRect;
import com.stedi.gyrshot.other.PaintFactory;

import java.util.List;

public class TargetsPointerLayer extends Layer implements GameLayer.TargetsListener, LayersView.OnNewTranslateValues {
    private final Paint paint = PaintFactory.create(Color.BLUE);

    private List<Target> gameTargets;

    private float gyroXOffset, gyroYOffset;

    private FloatRect drawRect;

    public TargetsPointerLayer() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public void onAddToLayersView(LayersView layersView) {
        layersView.addOnNewTranslateValuesListener(this);
    }

    @Override
    public void onRemoveFromLayersView(LayersView layersView) {
        layersView.removeOnNewTranslateValuesListener(this);
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
                if (target == null)
                    continue;

                float targetRealX = target.getX() + gyroXOffset;
                float targetRealY = target.getY() + gyroYOffset;

                float pointerX = drawRect.forceInLeftRight(targetRealX, target.getRadius());
                float pointerY = drawRect.forceInTopBottom(targetRealY, target.getRadius());

                if (pointerX != targetRealX || pointerY != targetRealY || CoreConfig.ALWAYS_SHOW_TARGET_POINTER)
                    canvas.drawCircle(pointerX, pointerY, 10, paint);
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
    public void onTargetDelete(Target target, boolean fromShot) {

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
