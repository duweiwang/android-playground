package com.example.wangduwei.demos.view.nestedscroll.androideventdispatch;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/12
 */

public class NestedChildView extends View {

    public NestedChildView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }
}
