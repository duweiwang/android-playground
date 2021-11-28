package com.example.customview.move_view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/10
 */

public abstract class MoveChildBase extends FrameLayout {
    protected View mChild;
    private float mLastX, mLastY;

    public MoveChildBase(@NonNull Context context) {
        this(context,null);
    }

    public MoveChildBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MoveChildBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mChild = getChildAt(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = x - mLastX;
                float moveY = y - mLastY;

                onEventMove(moveX, moveY);

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;

        }
        return true;
    }

    public void onActionUp() {
        //empty
    }

    public abstract void onEventMove(float moveX, float moveY);

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
