package com.example.customview.floatview.impl3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;


/**
 * <p> 拖拽容器
 *
 * @auther : wangduwei
 * @since : 2019/8/13  15:38
 **/
public class FloatBallDragLayout extends FrameLayout {

    private CircleCountDownView mCircleView;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private TextView mTextView;

    private boolean isExpand = true;

    private float mLastRawX;
    private float mLastRawY;
    private boolean isDrag = false;
    private int mRootMeasuredWidth = 0;
    private int mRootMeasuredHeight = 0;
    private int mRootTopY = 0;

    private IClickListener mClickListener;
    private ICloseListener mCloseListener;
    private IDragListener mDragListener;


    private ValueAnimator mOpen2SmallAnimation, mOpen2BigAnimation;
    private ValueAnimator mClose2SmallAnimation, mClose2BigAnimation;
    private ValueAnimator mTextOpenAnimation,mTextCloseAnimation;

    private static final int WIDTH_WITHOUT_TIME_DP = 50;//没有倒计时的时候View的宽度
    private static final int WIDTH_WITH_TIME_DP = 80;//存在倒计时的时候View的宽度

    private boolean mIsRunning = false;
    private long mTimeRemained = DEFAULT_TIME_MAX;
    private static final long DEFAULT_TIME_MAX = 10 * 60 * 1000;

    private static final int MSG_WHAT_UPDATE_PROGRESS = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT_UPDATE_PROGRESS) {
                notifyProgressUpdate();
                if (mTimeRemained <= 0) {
                    return;
                }
                sendEmptyMessageDelayed(MSG_WHAT_UPDATE_PROGRESS, 1000);
            }
        }
    };

    public FloatBallDragLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatBallDragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBallDragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addAnimateBg(context);

        addCircle(context);

        initAnimation();
    }

    public void startCountDown() {
        if (!mIsRunning){
            mHandler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
        }
        mIsRunning = true;
        mTextOpenAnimation.start();
    }

    public void setRemainTime(long remainTimeSeconds) {
        mTimeRemained = remainTimeSeconds * 1000;
    }

    public void stopCountDown() {
        mHandler.removeMessages(MSG_WHAT_UPDATE_PROGRESS);
        mTimeRemained = 0;
        mIsRunning = false;
        mTextCloseAnimation.start();
    }

    private void notifyProgressUpdate() {
        mTimeRemained -= 1000;
        if (mTimeRemained <= 0){
            stopCountDown();
        }
        mTextView.setText(long2String(mTimeRemained));
        Log.d("wdw", "进度更新 = " + long2String(mTimeRemained));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float mRawX = ev.getRawX();
        float mRawY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下
                isDrag = false;
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
                if (mRawX >= 0 && mRawX <= mRootMeasuredWidth - getMeasuredWidth()&&
                        mRawY >= mRootTopY  && mRawY <= (mRootMeasuredHeight + mRootTopY)) {
                    if (isExpand) {
                        changeSmall();
                    }
                    //手指X轴滑动距离
                    float differenceValueX = mRawX - mLastRawX;
                    //手指Y轴滑动距离
                    float differenceValueY = mRawY - mLastRawY;
                    //判断是否为拖动操作
                    if (!isDrag) {
                        if (Math.sqrt(differenceValueX * differenceValueX + differenceValueY * differenceValueY) < 2) {
                            isDrag = false;
                        } else {
                            isDrag = true;
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
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    ViewPropertyAnimator viewPropertyAnimator = this.animate()
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
                            });
//                    if (mRawY > mRootMeasuredHeight - dp2px(220)){
//                        viewPropertyAnimator.y(mRootMeasuredHeight - dp2px(220));
//                    }
                    viewPropertyAnimator.start();
                    if (mDragListener != null){
                        mDragListener.onDragFinish();
                    }
                } else {
                    if (isTouchInCircle(mLastRawX, mLastRawY) && mClickListener != null) {
                        mClickListener.onClick();
                    }
                    if (isTouchInImage(mLastRawX, mLastRawY) && mCloseListener != null) {
                        mCloseListener.onClose();
                    }
                }
                break;
        }
//        return isDrag ? isDrag : super.onTouchEvent(ev);
        return true;
    }


    private void changeBig() {
        if (mIsRunning) {
            mOpen2BigAnimation.start();
        } else {
            mOpen2SmallAnimation.start();
        }
        mCircleView.setHalfe(true);
        isExpand = true;
    }

    private void changeSmall() {
        if (mIsRunning) {
            mClose2BigAnimation.start();
        } else {
            mClose2SmallAnimation.start();
        }
        mCircleView.setHalfe(false);
        isExpand = false;
    }

    private void addCircle(Context context) {
        mCircleView = new CircleCountDownView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mCircleView, layoutParams);
    }

    private void initAnimation() {
        mOpen2SmallAnimation = ValueAnimator.ofInt(0, dp2px(WIDTH_WITHOUT_TIME_DP));
        mOpen2SmallAnimation.setDuration(100);
        mOpen2SmallAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = (int) valueAnimator.getAnimatedValue();
                requestLayout();
            }
        });
        mOpen2BigAnimation = ValueAnimator.ofInt(0, dp2px(WIDTH_WITH_TIME_DP));
        mOpen2BigAnimation.setDuration(100);
        mOpen2BigAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = (int) valueAnimator.getAnimatedValue();
                requestLayout();
            }
        });

        mClose2SmallAnimation = ValueAnimator.ofInt(dp2px(WIDTH_WITHOUT_TIME_DP), 0);
        mClose2SmallAnimation.setDuration(100);
        mClose2SmallAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                lp.width = animatedValue;
                requestLayout();
                FloatBallDragLayout.this.setTranslationX(-dp2px(WIDTH_WITHOUT_TIME_DP) + animatedValue);
            }
        });
        mClose2BigAnimation = ValueAnimator.ofInt(dp2px(WIDTH_WITH_TIME_DP), 0);
        mClose2BigAnimation.setDuration(100);
        mClose2BigAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams lp = (LayoutParams) mLinearLayout.getLayoutParams();
                int animatedValue =  (int) valueAnimator.getAnimatedValue();
                lp.width = animatedValue;
                requestLayout();

                FloatBallDragLayout.this.setTranslationX(-dp2px(WIDTH_WITH_TIME_DP) + animatedValue);
            }
        });

        mTextOpenAnimation = ValueAnimator.ofInt(0, dp2px(30));
        mTextOpenAnimation.setDuration(100);
        mTextOpenAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams textLp = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
                textLp.width = animatedValue;

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = dp2px(WIDTH_WITHOUT_TIME_DP) + animatedValue;

                requestLayout();
            }
        });

        mTextCloseAnimation = ValueAnimator.ofInt(dp2px(30),0);
        mTextCloseAnimation.setDuration(100);
        mTextCloseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                LinearLayout.LayoutParams textLp = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
                textLp.width = animatedValue;

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mLinearLayout.getLayoutParams();
                lp.width = dp2px(WIDTH_WITH_TIME_DP) - (dp2px(30) - animatedValue);

                requestLayout();
            }
        });
    }

    private void addAnimateBg(Context context) {
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setBackgroundColor(Color.parseColor("#66D309FF"));

        LinearLayout linearLayout = new LinearLayout(context);

        //add text
        mTextView = new TextView(context);
        mTextView.setTextSize(10);
        mTextView.setSingleLine(true);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setGravity(Gravity.RIGHT);
        mTextView.setText("");
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        linearLayout.addView(mTextView, textParam);

        //add close image
        mImageView = new ImageView(context);
        mImageView.setImageResource(R.drawable.star_game_float_close);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                dp2px(12), dp2px(12)
        );
        imageParam.leftMargin = dp2px(10);
        linearLayout.addView(mImageView, imageParam);

        LinearLayout.LayoutParams linearLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutLp.leftMargin = dp2px(CircleCountDownView.DEFAULT_INNER_RADIUS_DP);
        linearLayoutLp.rightMargin = dp2px(10);
        linearLayoutLp.gravity = Gravity.CENTER_VERTICAL;
        mLinearLayout.addView(linearLayout,linearLayoutLp);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP * 2)
        );
        lp.leftMargin = dp2px(CircleCountDownView.DEFAULT_OUTTER_RADIUS_DP);
        addView(mLinearLayout, lp);
    }


    public void setOnClickListener(IClickListener l) {
        mClickListener = l;
    }

    public void setOnCloseListener(ICloseListener l) {
        mCloseListener = l;
    }

    public void setDragListener(IDragListener l){
        mDragListener = l;
    }

    public interface IClickListener {
        void onClick();
    }

    public interface ICloseListener {
        void onClose();
    }

    public interface IDragListener{
        void onDragFinish();
    }

    private boolean isTouchInCircle(float x, float y) {
        int[] location = new int[2];
        mCircleView.getLocationOnScreen(location);
        if (x > location[0] && x < location[0] + mCircleView.getMeasuredWidth() &&
                y > location[1] && y < location[1] + mCircleView.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    private boolean isTouchInImage(float x, float y) {
        int[] location = new int[2];
        mImageView.getLocationOnScreen(location);
        if (x > location[0] && x < location[0] + mImageView.getMeasuredWidth() &&
                y > location[1] && y < location[1] + mImageView.getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private String long2String(long date) {
        long min = date / android.text.format.DateUtils.MINUTE_IN_MILLIS;
        long remainTime = date % android.text.format.DateUtils.MINUTE_IN_MILLIS;
        long sec = remainTime / android.text.format.DateUtils.SECOND_IN_MILLIS;
        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }
}
