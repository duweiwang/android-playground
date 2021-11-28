package com.example.wangduwei.project.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * @desc: 切换有缩放动画的ViewPager
 * @auther:duwei
 * @date:2018/11/14
 */
public class ZoomViewPager extends ViewPager {
    private static final int DURATION = 150;
    private static final int ZOOM_IN_DELAY = 200;
    private static final float SCALE = 0.95f;

    private float mScale = 1f;
    private int mScrollState = SCROLL_STATE_IDLE;
    private boolean mHasZoomOut;
    private OnPageChangedWrapper mOnPageChangedWrapper;
    private Runnable mZoomInRunnable = new Runnable() {
        @Override
        public void run() {
            zoom(false);
            mHasZoomOut = false;
        }
    };

    public ZoomViewPager(Context context) {
        super(context);
        init();
    }

    public ZoomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOnPageChangedWrapper = new OnPageChangedWrapper();
        super.setOnPageChangeListener(mOnPageChangedWrapper);
    }

    private class OnPageChangedWrapper implements OnPageChangeListener {
        private OnPageChangeListener mOnPageChangeListener;

        public void setOnPageChangeListener(OnPageChangeListener l) {
            mOnPageChangeListener = l;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            if (positionOffset > 0 && !mHasZoomOut && mScrollState != SCROLL_STATE_SETTLING) {
                zoom(true);
                mHasZoomOut = true;
            } else if (positionOffset == 0 && mHasZoomOut) {
                postDelayed(mZoomInRunnable, ZOOM_IN_DELAY);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
            mScrollState = state;
            if (state == SCROLL_STATE_SETTLING) {
                if (mHasZoomOut) {
                    postDelayed(mZoomInRunnable, ZOOM_IN_DELAY);
                }
            } else if (state == SCROLL_STATE_DRAGGING) {
                removeCallbacks(mZoomInRunnable);
            }
        }
    }

    private class ZoomAnimation extends Animation {
        private final boolean mOut;
        private final float mStartScale;

        public ZoomAnimation(boolean out, float scale) {
            mOut = out;
            mStartScale = scale;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            float end;
            if (mOut) {
                end = SCALE;
            } else {
                end = 1;
            }
            mScale = mStartScale + (end - mStartScale) * interpolatedTime;
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                view.setScaleX(mScale);
                view.setScaleY(mScale);
//                ViewHelper.setScaleX(view, mScale);//wdw：原来用的nineold
//                ViewHelper.setScaleY(view, mScale);
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(DURATION);
            setFillAfter(true);
            if (mOut) {
                setInterpolator(new DecelerateInterpolator());
            } else {
                setInterpolator(new AccelerateInterpolator());
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangedWrapper.setOnPageChangeListener(listener);
    }

    private void zoom(boolean out) {
        clearAnimation();
        startAnimation(new ZoomAnimation(out, mScale));
    }
}
