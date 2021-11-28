package com.example.wangduwei.demos.gles.shape.circle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.example.wangduwei.demos.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/1
 */

public class CircleContentLayout extends FrameLayout {

    private SeekBar seekBarX;
    private SeekBar seekBarY;
    private CircleView circleView;

    public CircleContentLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CircleContentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.opengles_circle, null);
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, lp);

        circleView = findViewById(R.id.circlerview);

        seekBarX = findViewById(R.id.seekbar_x);
        seekBarX.setOnSeekBarChangeListener(circleView);

        seekBarY = findViewById(R.id.seekbar_y);
        seekBarY.setOnSeekBarChangeListener(circleView);

    }


}
