package com.example.customview.move_view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @desc: change this child's margin params
 * @auther:duwei
 * @date:2018/4/10
 */

public class MoveChild_LayoutParam extends MoveChildBase {

    public MoveChild_LayoutParam(@NonNull Context context) {
        super(context);
    }

    public MoveChild_LayoutParam(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_LayoutParam(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        FrameLayout.LayoutParams lp = (LayoutParams) mChild.getLayoutParams();
        lp.leftMargin += moveX;
        lp.topMargin += moveY;
        mChild.setLayoutParams(lp);
        Log.d("wdw", "MoveChild_LayoutParam# X = " + mChild.getX() + ", Y = " + mChild.getY());
    }
}
