package com.example.wangduwei.demos.view.event.log;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/18
 */
public class LogEventView extends View {
    public LogEventView(Context context) {
        super(context);
    }

    public LogEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("wdw-event", "View::dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
//        return true;//这里虽然return true了，但是后面的move、up还是继续走父层的方法。
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("wdw-event", "View::onTouchEvent");
//        if (event.getAction() == MotionEvent.ACTION_DOWN){
//            return true;
//        }
        return super.onTouchEvent(event);

    }
}
