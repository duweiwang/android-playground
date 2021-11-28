package com.example.wangduwei.demos.view.event.outterintercepte;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/19
 */
public class Group extends FrameLayout {
    private int mLastX;
    private int mMovedX;

    public Group(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        if (MotionEvent.ACTION_MOVE == ev.getAction()) {
            mMovedX = x - mLastX;
        }
        mLastX = x;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View child = getChildAt(0);
        if (outSideParent(child)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean outSideParent(View child) {
        if (child.getLeft() < 0 && mMovedX < 0 ||
                child.getRight() > getMeasuredWidth() && mMovedX > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            offsetLeftAndRight(mMovedX);
        }
        return true;
    }
}
