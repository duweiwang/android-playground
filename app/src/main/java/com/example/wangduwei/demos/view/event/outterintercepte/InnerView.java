package com.example.wangduwei.demos.view.event.outterintercepte;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @desc:
 * @auther:duwei
 * @date:2019/3/19
 */
public class InnerView extends View {
    private int lastAction = -1;
    int mLastX;
    int mMovedX;

    public InnerView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        logAction(event);
        int x = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mMovedX = x - mLastX;
                break;
        }
        mLastX = x;
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            offsetLeftAndRight(mMovedX);
        }
        return true;
    }

    private void logAction(MotionEvent event) {
        String action = "";
        if (lastAction == event.getAction()){
            return;
        }
        lastAction = event.getAction();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
        }
        Log.d("wdw-event", "--" + action);
    }
}
