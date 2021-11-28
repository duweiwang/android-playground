package com.example.customview.move_view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.widget.Scroller;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/10
 */

public class MoveChild_Scroller extends MoveChildBase {
    private Scroller mScroller;

    public MoveChild_Scroller(@NonNull Context context) {
        this(context,null);

    }

    public MoveChild_Scroller(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MoveChild_Scroller(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public void onEventMove(float moveX, float moveY) {
        scrollBy((int)-moveX,(int)-moveY);
    }

    @Override
    public void onActionUp() {
        super.onActionUp();
//        mScroller.startScroll(getScrollX(),getScrollY(),-getScrollX(),-getScrollY());//scroll back
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
        }
        invalidate();
    }
}
