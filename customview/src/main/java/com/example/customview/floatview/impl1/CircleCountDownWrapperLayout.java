package com.example.customview.floatview.impl1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.floatview.CircleCountDownView;
import com.example.customview.floatview.ICountDown;

/**
 * <p>组合进度圆圈和文本
 *
 * @auther : wangduwei
 * @since : 2019/8/15  18:11
 **/
public class CircleCountDownWrapperLayout extends FrameLayout {
    private CircleCountDownView mCountDownView;
    //    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private static final int DEFAULT_LAYOUT_WIDTH_DP = 47;
    private ValueAnimator bigAnimation, smallAnimation;

    private boolean isExpand = true;

    private float mLastRawX;
    private float mLastRawY;
    private boolean isDrug = false;
    private int mRootMeasuredWidth = 0;
    private int mRootMeasuredHeight = 0;
    private int mRootTopY = 0;


    public CircleCountDownWrapperLayout(@NonNull Context context) {
        this(context, null);
    }

    public CircleCountDownWrapperLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCountDownWrapperLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addExpandableView(context);

        addCircleView(context);

        initAnimator();
    }

    public void startAnimation() {
//        if (isExpand) {
//            smallAnimation.start();
//            isExpand = false;
//        } else {
//            bigAnimation.start();
//            isExpand = true;
//        }
    }

    private void changeBig() {
        bigAnimation.start();
        isExpand = true;
    }

    private void changeSmall() {
        smallAnimation.start();
        isExpand = false;
    }

    private void initAnimator() {
        bigAnimation = ValueAnimator.ofInt(0, dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP * 2));
        bigAnimation.setDuration(150);
        bigAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });

        smallAnimation = ValueAnimator.ofInt(dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP * 2), 0);
        smallAnimation.setDuration(150);
        smallAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
    }

    private void addExpandableView(Context context) {
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setBackgroundColor(Color.WHITE);

        mImageView = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(12), dp2px(12));
        mImageView.setBackgroundColor(Color.RED);//TODO
        lp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mLinearLayout.addView(mImageView, lp);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                dp2px(DEFAULT_LAYOUT_WIDTH_DP),
                dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP * 2)
        );
        layoutParams.leftMargin = dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP);
        addView(mLinearLayout, layoutParams);
    }

    private void addCircleView(Context context) {
        mCountDownView = new CircleCountDownView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mCountDownView, layoutParams);
    }

    public ICountDown getCircleView() {
        return mCountDownView;
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //判断是否需要滑动
        //当前手指的坐标
        float mRawX = ev.getRawX();
        float mRawY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                Log.d("wdw", "ACTION_DOWN...");
                isDrug = false;
                //记录按下的位置
                mLastRawX = mRawX;
                mLastRawY = mRawY;
                ViewGroup mViewGroup = (ViewGroup) getParent();
                if (mViewGroup != null) {
                    int[] location = new int[2];
                    mViewGroup.getLocationInWindow(location);
                    //获取父布局的高度
                    mRootMeasuredHeight = mViewGroup.getMeasuredHeight();
                    mRootMeasuredWidth = mViewGroup.getMeasuredWidth();
                    //获取父布局顶点的坐标
                    mRootTopY = location[1];
                }
                break;
            case MotionEvent.ACTION_MOVE://手指滑动
                Log.d("wdw", "ACTION_MOVE...");
                if (mRawX >= 0 && mRawX <= mRootMeasuredWidth && mRawY >= mRootTopY && mRawY <= (mRootMeasuredHeight + mRootTopY)) {
                    if (isExpand) {
                        changeSmall();
                    }
                    //手指X轴滑动距离
                    float differenceValueX = mRawX - mLastRawX;
                    //手指Y轴滑动距离
                    float differenceValueY = mRawY - mLastRawY;
                    //判断是否为拖动操作
                    if (!isDrug) {
                        if (Math.sqrt(differenceValueX * differenceValueX + differenceValueY * differenceValueY) < 2) {
                            isDrug = false;
                        } else {
                            isDrug = true;
                        }
                    }
                    //获取手指按下的距离与控件本身X轴的距离
                    float ownX = getX();
                    //获取手指按下的距离与控件本身Y轴的距离
                    float ownY = getY();
                    //理论中X轴拖动的距离
                    float endX = ownX + differenceValueX;
                    //理论中Y轴拖动的距离
                    float endY = ownY + differenceValueY;
                    //X轴可以拖动的最大距离
                    float maxX = mRootMeasuredWidth - getWidth();
                    //Y轴可以拖动的最大距离
                    float maxY = mRootMeasuredHeight - getHeight();
                    //X轴边界限制
                    endX = endX < 0 ? 0 : endX > maxX ? maxX : endX;
                    //Y轴边界限制
                    endY = endY < 0 ? 0 : endY > maxY ? maxY : endY;
                    //开始移动
                    setX(endX);
                    setY(endY);
                    //记录位置
                    mLastRawX = mRawX;
                    mLastRawY = mRawY;
                }

                break;
            case MotionEvent.ACTION_UP://手指离开
                //根据自定义属性判断是否需要贴边
                //判断是否为点击事件
                Log.d("wdw", "ACTION_UP...");
                if (isDrug) {
                    CircleCountDownWrapperLayout.this.animate()
                            .setDuration(500)
                            .x(mRootMeasuredWidth - getMeasuredWidth())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (!isExpand) {
                                        changeBig();
                                    }
                                }
                            })
                            .start();
                }
                break;
        }
        //是否拦截事件
        return /*isDrug ? isDrug : */true;
    }
}
