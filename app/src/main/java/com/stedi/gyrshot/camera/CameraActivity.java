package com.stedi.gyrshot.camera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.ViewGroup;

import com.stedi.gyrshot.constants.AppConfig;

public abstract class CameraActivity extends Activity {
    private Camera camera;
    private CameraPreview cameraPreview;

    private boolean hasCamera;
    private boolean allowCamera = true;

    private int cameraId = -1;

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
        if (allowCamera && !isCameraOpen())
            camera = initCamera(getPreviewContainer());
        onCameraOpen(isCameraOpen());
    }

    private void tryToReleaseCamera() {
        if (isCameraOpen()) {
            cameraPreview.release();
            camera.release();
            getPreviewContainer().removeView(cameraPreview);
            cameraPreview = null;
            camera = null;
            onCameraRelease();
        }
    }

    private Camera initCamera(ViewGroup previewContainer) {
        if (!AppConfig.ALLOW_CAMERA)
            return null;
        Camera c = getCameraInstance();
        if (c != null) {
            cameraPreview = new CameraPreview(this, c);
            previewContainer.removeAllViews();
            previewContainer.addView(cameraPreview);
        }
        return c;
    }

    private Camera getCameraInstance() {
        if (hasCamera) {
            try {
                if (cameraId != -1)
                    return Camera.open(cameraId);
                for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    Camera.getCameraInfo(cameraId, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        this.cameraId = cameraId;
                        return Camera.open(cameraId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
