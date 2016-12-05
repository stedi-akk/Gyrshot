package com.stedi.gyrshot.camera;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.ViewGroup;

public abstract class CameraActivity extends Activity {
    private Camera camera;

    @Override
    protected void onStart() {
        super.onStart();
        boolean result = initCamera(onCreateCameraPreview());
        onCameraInit(result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    public void onCameraInit(boolean result) {
    }

    protected abstract ViewGroup onCreateCameraPreview();

    private boolean initCamera(ViewGroup previewContainer) {
        Camera c = getCameraInstance();
        if (c != null) {
            CameraPreview cameraPreview = new CameraPreview(this, c);
            previewContainer.addView(cameraPreview);
            return true;
        }
        return false;
    }

    private Camera getCameraInstance() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            try {
                for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    Camera.getCameraInfo(cameraId, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                        return Camera.open(cameraId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void startPreview() {
        if (camera != null)
            camera.startPreview();
    }

    private void stopPreview() {
        if (camera != null)
            camera.stopPreview();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
