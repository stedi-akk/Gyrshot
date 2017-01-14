package com.stedi.gyrshot.camera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.ViewGroup;

public abstract class CameraActivity extends Activity {
    private Camera camera;
    private int cameraId = -1;

    private CameraPreview cameraPreview;

    private boolean hasCamera;
    private boolean allowCamera = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryToOpenCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tryToReleaseCamera();
    }

    public void allowCamera() {
        allowCamera = true;
        tryToOpenCamera();
    }

    public void disableCamera() {
        allowCamera = false;
        tryToReleaseCamera();
    }

    public boolean isCameraOpen() {
        return camera != null;
    }

    public void onCameraOpen(boolean result) {
    }

    public void onCameraRelease() {
    }

    protected abstract ViewGroup getPreviewContainer();

    private void tryToOpenCamera() {
        if (!hasCamera || !allowCamera) {
            onCameraOpen(false);
            return;
        }

        if (isCameraOpen()) {
            onCameraOpen(true);
        }

        CameraWorker.getInstance().openCamera(cameraId, new CameraWorker.OnOpenCallback() {
            @Override
            public void onOpen(CameraWorker.CameraOpenResult result) {
                camera = result.camera;
                cameraId = result.cameraId;

                if (camera != null) {
                    cameraPreview = new CameraPreview(CameraActivity.this, camera);
                    cameraPreview.onStartPreviewCallback(new CameraPreview.OnStartPreviewCallback() {
                        @Override
                        public void onStartPreview() {
                            onCameraOpen(true);
                        }
                    });
                    getPreviewContainer().removeAllViews();
                    getPreviewContainer().addView(cameraPreview);
                } else {
                    onCameraOpen(false);
                }
            }
        });
    }

    private void tryToReleaseCamera() {
        if (!hasCamera)
            return;

        onCameraRelease();

        if (cameraPreview != null) {
            cameraPreview.release();
            getPreviewContainer().removeView(cameraPreview);
            cameraPreview = null;
        }

        CameraWorker.getInstance().releaseCamera();
        camera = null;
    }
}
