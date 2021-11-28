package com.example.wangduwei.demos.view.event.log;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/18
 */
public class LogEventGroup extends FrameLayout {
    public LogEventGroup(@NonNull Context context) {
        super(context);
    }

    public LogEventGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogEventGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("wdw-event", "Group::dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
//        return true;//事件结束
//        return false;//走Activity
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("wdw-event", "Group::onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("wdw-event", "Group::onTouchEvent");
        return super.onTouchEvent(event);
    }
}
