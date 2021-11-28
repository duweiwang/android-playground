package com.example.customview.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @desc: 简易ViewPager 演示了Scroller的使用
 * @auther:duwei
 * @date:2018/3/13
 */

public class MyViewPager extends ViewGroup {
    private int width;
    private int scrollX;
    //    private float downX;
    private float moveX;
    private float lastX;
    private int touchSlop;
    private int leftLimit;
    private int rightLimit;
    private Context mContext;
    private Scroller mScroller;
    private final String TAG = "stay4it";

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        width = getResources().getDisplayMetrics().widthPixels;
        mScroller = new Scroller(mContext);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(mContext);
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = getChildAt(i);
//            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int left = i * childView.getMeasuredWidth();
                int top = 0;
                int right = (i + 1) * childView.getMeasuredWidth();
                int bottom = childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
            }
            leftLimit = getChildAt(0).getLeft();
            rightLimit = getChildAt(childCount - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        float x = ev.getRawX();
//        switch (ev.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////                downX = ev.getRawX();
////                lastX = downX;
////                break;
//            case MotionEvent.ACTION_MOVE:
////                moveX = ev.getRawX();
//                float moveDistance = Math.abs(x - lastX);
////                lastX = moveX;
//                if (moveDistance > touchSlop) {
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        lastX = x;
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getRawX();
//                break;
                return true;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getRawX();
                int moveDistanceX = (int) (lastX - moveX);
                scrollX = getScrollX();
                if (scrollX + moveDistanceX < leftLimit) {
                    scrollTo(leftLimit, 0);
                    return true;
                }

                if (scrollX + moveDistanceX + width > rightLimit) {
                    scrollTo(rightLimit - width, 0);
                    return true;
                }
                scrollBy(moveDistanceX, 0);
                lastX = moveX;
//--------------------------------------------------------
//                float offset = ev.getRawX() - lastX;
//                scrollTo(-(int) offset, 0);
                break;
            case MotionEvent.ACTION_UP:
                scrollX = getScrollX();
                int index = (scrollX + width / 2) / width;
                int distanceX = width * index - scrollX;
                mScroller.startScroll(scrollX, 0, distanceX, 0);
                invalidate();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
