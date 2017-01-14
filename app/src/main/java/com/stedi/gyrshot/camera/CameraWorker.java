package com.stedi.gyrshot.camera;

import android.hardware.Camera;
import android.os.Handler;

import com.stedi.gyrshot.constants.AppConfig;

public class CameraWorker {
    private final Handler uiHandler = new Handler();

    private static CameraWorker instance;

    private OnOpenCallback onOpenCallback;

    private CameraOpenResult lastResult;

    private boolean openInProgress;

    public interface OnOpenCallback {
        void onOpen(CameraOpenResult result);
    }

    public class CameraOpenResult {
        public final Camera camera;
        public final int cameraId;

        public CameraOpenResult(Camera camera, int cameraId) {
            this.camera = camera;
            this.cameraId = cameraId;
        }
    }

    private CameraWorker() {
    }

    public static CameraWorker getInstance() {
        if (instance == null)
            instance = new CameraWorker();
        return instance;
    }

    public void openCamera(OnOpenCallback callback) {
        openCamera(-1, callback);
    }

    public void openCamera(final int selectedCameraId, OnOpenCallback callback) {
        if (openInProgress)
            return;

        openInProgress = true;
        onOpenCallback = callback;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final CameraOpenResult result = tryToOpenCamera(selectedCameraId);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        openInProgress = false;
                        lastResult = result;
                        if (onOpenCallback != null) {
                            onOpenCallback.onOpen(lastResult);
                            onOpenCallback = null;
                        } else {
                            releaseCamera();
                        }
                    }
                });
            }

            private CameraOpenResult tryToOpenCamera(int selectedCameraId) {
                if (AppConfig.ALLOW_CAMERA) {
                    try {
                        if (selectedCameraId != -1)
                            return new CameraOpenResult(Camera.open(selectedCameraId), selectedCameraId);
                        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                            Camera.getCameraInfo(cameraId, cameraInfo);
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                                return new CameraOpenResult(Camera.open(cameraId), cameraId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return new CameraOpenResult(null, selectedCameraId);
            }
        }).start();
    }

    public void releaseCamera() {
        if (onOpenCallback != null)
            onOpenCallback = null;

        if (lastResult != null) {
            final Camera camera = lastResult.camera;
            lastResult = null;
            if (camera != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        camera.release();
                    }
                }).start();
            }
        }
    }
}
