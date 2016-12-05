package com.stedi.gyrshot.camera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.ViewGroup;

public abstract class CameraActivity extends Activity {
    private boolean hasCamera;
    private int cameraId = -1;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onCameraInit(initCamera(getPreviewContainer()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void onCameraInit(boolean result) {
    }

    protected abstract ViewGroup getPreviewContainer();

    private boolean initCamera(ViewGroup previewContainer) {
        Camera c = getCameraInstance();
        if (c != null) {
            CameraPreview cameraPreview = new CameraPreview(this, c);
            previewContainer.removeAllViews();
            previewContainer.addView(cameraPreview);
            return true;
        }
        return false;
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

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
