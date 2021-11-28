package com.example.wangduwei.demos.gles.shape.circle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.SeekBar;

import com.example.wangduwei.demos.R;

public class CircleView extends GLSurfaceView implements SeekBar.OnSeekBarChangeListener {
    private GLSurfaceView.Renderer mRender;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRender = new CircleRender();
        setRenderer(mRender);
    }

    private int lastProgress = 0;
    private float current = 0;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        float changed = lastProgress - progress;
        Log.d("wdw-gl", "current = " + current + ",changed = " + changed);
        if (changed > 0) {//what to move left
            current -= Math.abs(changed);
        } else {//what to move right
            current += Math.abs(changed);
        }
        lastProgress = progress;

        switch (seekBar.getId()) {
            case R.id.seekbar_x:
                Log.d("wdw-gl", "final= " + current / 100f);
                ((Circle) ((CircleRender) mRender).getCircle()).translateX(current / 100f);
                requestRender();
                break;
            case R.id.seekbar_y:
                ((Circle) ((CircleRender) mRender).getCircle()).translateY(current / 100f);
                requestRender();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
