package com.example.customview.floatview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * <p> 圆圈倒计时View
 *
 * @auther : wangduwei
 * @since : 2019/8/13  16:35
 **/
public class CircleCountDownView extends View implements ICountDown {

    private boolean mIsShowProgress = false;
    private float mSweepAngle;
    private long mStartTime;
    private float mProgress;
    private long mMaxTime = DEFAULT_TIME_MAX;

    private ICountDownFinishListener mListener;

    private Paint mBgPaint, mCirclePaint;
    private RectF mRectF = new RectF();

    /*color white*/
    private static final String DEFAULT_COLOR_OUTTER_BG = "#FFFFFF";
    /*color light blue*/
    private static final String DEFAULT_COLOR_INNER_BG = "#62A4ED";
    /*color for countdown circle line*/
    private static final String DEFAULT_COLOR_CIRCLE_BG = "#F5A623";

    public static final float DEFAULT_OUTTER_RADIUS_DP = 21f;//84/4=21
    private static final float DEFAULT_INNER_RADIUS_DP = 19;//76/4=19
    private static final float DEFAULT_CIRCLE_WIDTH_DP = 1.95f;//61.2-53.4 = /4 = 1.95
    private static final float DEFAULT_CIRCLE_RECT_WIDTH_DP = 30.6f;//61.2/2=30.6dp

    //10 minutes
    private static final long DEFAULT_TIME_MAX = 10 * 60 * 1000;

    private static final int MSG_WHAT_UPDATE_PROGRESS = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_WHAT_UPDATE_PROGRESS) {
                notifyProgressUpdate();
                sendEmptyMessageDelayed(MSG_WHAT_UPDATE_PROGRESS, 1000);
            }
        }
    };


    public CircleCountDownView(Context context) {
        this(context, null);
    }

    public CircleCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //init paint first
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.parseColor(DEFAULT_COLOR_OUTTER_BG));
        mBgPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor(DEFAULT_COLOR_CIRCLE_BG));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(dp2px(DEFAULT_CIRCLE_WIDTH_DP));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(
                dp2px(DEFAULT_CIRCLE_WIDTH_DP + DEFAULT_CIRCLE_WIDTH_DP / 2),
                dp2px(DEFAULT_CIRCLE_WIDTH_DP + DEFAULT_CIRCLE_WIDTH_DP / 2),
                dp2px(DEFAULT_CIRCLE_WIDTH_DP + DEFAULT_CIRCLE_RECT_WIDTH_DP - DEFAULT_CIRCLE_WIDTH_DP / 2),
                dp2px(DEFAULT_CIRCLE_WIDTH_DP + DEFAULT_CIRCLE_RECT_WIDTH_DP - DEFAULT_CIRCLE_WIDTH_DP / 2)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawOutterBg(canvas);
        drawTimeArc(canvas);
        drawInnerBg(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }


    @Override
    public void start() {
        mStartTime = SystemClock.elapsedRealtime();
        mProgress = 0;
        mHandler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
    }

    @Override
    public void stop() {
        mProgress = 0;
        mHandler.removeMessages(MSG_WHAT_UPDATE_PROGRESS);
    }

    @Override
    public void setShowProgress(boolean isShow) {
        mIsShowProgress = isShow;
    }

    @Override
    public void setTimeRemained(long remainedTime) {

    }

    @Override
    public void setCountDownTime(long timeMillis) {
        mMaxTime = timeMillis;
    }

    @Override
    public void setCountDownTime(int minutes) {
        mMaxTime = minutes * 60 * 1000;
    }

    @Override
    public void setFinishListener(ICountDownFinishListener l) {
        mListener = l;
    }

    private void notifyProgressUpdate() {
        mProgress = ((float) ((SystemClock.elapsedRealtime() - mStartTime)) / mMaxTime);
        Log.d("wdw", "进度更新 Progress = " + mProgress);
        if (mProgress >= 1) {
            mSweepAngle = 0;
            stop();
            notifyFinish();
        } else {
            mSweepAngle = (1 - mProgress) * 360;
        }
        invalidate();
    }

    private void drawInnerBg(Canvas canvas) {
        changePaintColor(DEFAULT_COLOR_INNER_BG);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp2px(DEFAULT_INNER_RADIUS_DP), mBgPaint);
    }

    private void drawOutterBg(Canvas canvas) {
        changePaintColor(DEFAULT_COLOR_OUTTER_BG);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, dp2px(DEFAULT_OUTTER_RADIUS_DP), mBgPaint);
    }

    /*绘制圆弧*/
    private void drawTimeArc(Canvas canvas) {
        if (!mIsShowProgress) return;
        canvas.drawArc(mRectF, -90F, -mSweepAngle, false, mCirclePaint);
    }

    private void changePaintColor(String color) {
        mBgPaint.setColor(Color.parseColor(color));
    }

    private void notifyFinish() {
        if (mListener != null) {
            mListener.onFinish();
        }
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    private int measureWidth(int widthSpec) {
        int mode = MeasureSpec.getMode(widthSpec);
        int size = MeasureSpec.getSize(widthSpec);
        int value = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                value = size;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST://textSize
                value = dp2px(2 * DEFAULT_OUTTER_RADIUS_DP);
                break;
        }
        return value + getPaddingLeft() + getPaddingRight();
    }
}
