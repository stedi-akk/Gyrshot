package com.stedi.gyrshot.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Camera camera;

    private OnStartPreviewCallback startPreviewCallback;

    public interface OnStartPreviewCallback {
        void onStartPreview();
    }

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    public void onStartPreviewCallback(OnStartPreviewCallback callback) {
        this.startPreviewCallback = callback;
    }

    public void release() {
        camera.setPreviewCallback(null);
        holder.removeCallback(this);
        startPreviewCallback = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
            if (startPreviewCallback != null)
                startPreviewCallback.onStartPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startPreviewCallback = null;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (this.holder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(this.holder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}