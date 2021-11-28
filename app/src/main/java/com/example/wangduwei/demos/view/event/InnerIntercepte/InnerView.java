package com.example.wangduwei.demos.view.event.InnerIntercepte;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @desc: 需要记住：parent.requestDisallowInterceptTouchEvent只能控制父类的{@code onInterceptTouchEvent}方法是否执行
 * @auther:duwei
 * @date:2019/3/18
 */
public class InnerView extends View {
    private int mLastX;
    private int movedX;

    public InnerView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewGroup parent = (ViewGroup) getParent();
        int x = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                movedX = x - mLastX;
                //这种情况父亲处理
                if ((getLeft() <=0 && movedX < 0) ||
                        (getRight() >parent.getMeasuredWidth() && movedX > 0)) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        mLastX = x;
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE)
            offsetLeftAndRight(movedX);
        return true;
        //不反回true事件就回最上层了！
    }
}
