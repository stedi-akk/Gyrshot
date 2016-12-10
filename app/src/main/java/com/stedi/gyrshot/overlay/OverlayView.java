package com.stedi.gyrshot.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.stedi.gyrshot.Mode;
import com.stedi.gyrshot.R;

public class OverlayView extends FrameLayout implements View.OnClickListener {
    private Mode mode;
    private Listener listener;

    public interface Listener {
        void onShot();

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
        findViewById(R.id.overlay_view_btn_camera).setOnClickListener(settingsClickListener);
        findViewById(R.id.overlay_view_btn_sounds).setOnClickListener(settingsClickListener);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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
            if (v.getId() == R.id.overlay_view_btn_camera) {
                listener.onCameraClick();
            } else if (v.getId() == R.id.overlay_view_btn_sounds) {
                listener.onSoundsClick();
            }
        }
    };
}
