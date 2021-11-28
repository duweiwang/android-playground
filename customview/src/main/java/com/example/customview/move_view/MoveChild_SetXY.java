package com.example.customview.move_view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/11
 */

public class MoveChild_SetXY extends MoveChildBase {

    public MoveChild_SetXY(@NonNull Context context) {
        super(context);
    }

    public MoveChild_SetXY(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveChild_SetXY(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mChild.setX(event.getRawX());//setX==setTranslationX(x-mLeft)
//        mChild.setY(event.getRawY());
//        return true;
//    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        mChild.setX(mChild.getX() + moveX);
        mChild.setY(mChild.getY() + moveY);
    }
}
