package com.example.customview.move_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @desc: use {@code layout()}method to change the view position follow your finger
 * @auther:duwei
 * @date:2018/4/10
 */

public class MoveChild_Layout extends MoveChildBase {

    public MoveChild_Layout(@NonNull Context context) {
        super(context);
    }

    public MoveChild_Layout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_Layout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        int movex = (int) moveX;
        int movey = (int) moveY;
        mChild.layout(mChild.getLeft() + movex,
                mChild.getTop() + movey,
                mChild.getRight() + movex,
                mChild.getBottom() + movey);
        Log.d("wdw", "MoveChild_Layout# X = " + mChild.getX() + ", Y = " + mChild.getY());
    }
}
