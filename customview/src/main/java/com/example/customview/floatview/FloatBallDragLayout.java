package com.example.customview.floatview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <p> 主要用于处理事件
 *
 * @auther : wangduwei
 * @since : 2019/8/13  15:38
 **/
public class FloatBallDragLayout extends FrameLayout implements IDraggableFloatBall {


    public FloatBallDragLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatBallDragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBallDragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

}
