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

public class MoveChild_ScrollBy extends MoveChildBase {
    public MoveChild_ScrollBy(@NonNull Context context) {
        super(context);
    }

    public MoveChild_ScrollBy(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_ScrollBy(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        scrollBy((int) -moveX, (int) -moveY);
        Log.d("wdw", "MoveChild_ScrollBy# X = " + mChild.getX() + ", Y = " + mChild.getY());
    }
}
