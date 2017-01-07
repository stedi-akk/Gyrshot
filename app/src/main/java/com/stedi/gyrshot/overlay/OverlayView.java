package com.stedi.gyrshot.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.stedi.gyrshot.R;

public class OverlayView extends FrameLayout implements View.OnClickListener {
    private Listener listener;

    private View btnBack;
    private View btnCamera;
    private View btnSounds;

    public interface Listener {
        void onShot();

        void onBackClick();

        void onCameraClick();

        void onSoundsClick();
    }

    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.overlay_view, this, true);
        setOnClickListener(this);
        btnBack = findViewById(R.id.overlay_view_btn_back);
        btnBack.setOnClickListener(settingsClickListener);
        btnCamera = findViewById(R.id.overlay_view_btn_camera);
        btnCamera.setOnClickListener(settingsClickListener);
        btnSounds = findViewById(R.id.overlay_view_btn_sounds);
        btnSounds.setOnClickListener(settingsClickListener);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setBackButtonVisible(final boolean visible) {
        btnBack.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (listener == null)
            return;
        listener.onShot();
    }

    private OnClickListener settingsClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener == null)
                return;
            switch (v.getId()) {
                case R.id.overlay_view_btn_back:
                    listener.onBackClick();
                    break;
                case R.id.overlay_view_btn_camera:
                    listener.onCameraClick();
                    break;
                case R.id.overlay_view_btn_sounds:
                    listener.onSoundsClick();
            }
        }
    };
}
