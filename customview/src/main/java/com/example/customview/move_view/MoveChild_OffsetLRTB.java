package com.example.customview.move_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/10
 */

public class MoveChild_OffsetLRTB extends MoveChildBase {
    public MoveChild_OffsetLRTB(@NonNull Context context) {
        super(context);
    }

    public MoveChild_OffsetLRTB(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_OffsetLRTB(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        mChild.offsetLeftAndRight((int) moveX);
        mChild.offsetTopAndBottom((int) moveY);

        Log.d("wdw", "width = " + mChild.getWidth() + ",measureWidth = " + mChild.getMeasuredWidth());
        Log.d("wdw", "x = " + mChild.getX() + ",y = " + mChild.getY());
    }
}
