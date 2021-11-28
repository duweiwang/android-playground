package com.example.customview.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * <p>
 * 演示Scroller滑动。越滑越慢。阻力。手势处理
 * </p>
 *
 * @auther:duwei
 * @date:2018/3/12
 */

public class BounceableRelativeLayout extends RelativeLayout {
    private Scroller mScroller;
    private GestureDetector mGestureDetector;

    public BounceableRelativeLayout(Context context) {
        this(context, null);
    }

    public BounceableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new GestureListenerImpl());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.d("wdw", "-----UP------------------");
                reset(0, 0);
                break;
            default:
                return mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


    class GestureListenerImpl implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int disY = (int) ((distanceY - 0.5) / 2);//使手指下滑和布局下移的频率不一致，有种下拉很“困难”的感觉。
            Log.d("wdw", "onScroll----- ==" + disY);
            beginScroll(0, disY);
            return false;
        }

        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float x, float y) {
            return false;
        }

    }


    protected void reset(int x, int y) {
        int dx = x - mScroller.getFinalX();
        int dy = y - mScroller.getFinalY();
        beginScroll(dx, dy);
    }

    protected void beginScroll(int dx, int dy) {
        Log.d("wdw", "getFinalX-1 = " + mScroller.getFinalX() + ", getFinalY-1=" + mScroller.getFinalY());
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        Log.d("wdw", "getFinalX-2 = " + mScroller.getFinalX() + ", getFinalY-2=" + mScroller.getFinalY());
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d("wdw", "getCurrX = " + mScroller.getCurrX() + " ,getCurrY = " + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }
}
