package com.example.wangduwei.demos.view.nestedscroll.androideventdispatch;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @desc: 使用外部拦截法，当此view没到达顶部时自己滑动，否则孩子滑动
 * @auther:duwei
 * @date:2018/4/12
 */

public class NestedParentView extends FrameLayout {

    public NestedParentView(@NonNull Context context) {
        super(context);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            boolean isUp = getTop() != 0 && ev.getY() - downY < 0;
            boolean isDown = getBottom() != 0 && ev.getY() - downY > 0;
            if (isUp || isDown) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    float downY;
    float movedY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                movedY = event.getY() - downY;
                setTranslationY(getY() + movedY);
                return true;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onTouchEvent(event);

    }
}
