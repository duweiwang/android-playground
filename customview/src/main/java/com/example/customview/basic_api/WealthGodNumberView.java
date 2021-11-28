package com.example.customview.basic_api;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * <p>
 * 财神抢币但数字滚动View
 * </p>
 *
 * @auther: davewang
 * @since: 2019/7/4
 **/
public class WealthGodNumberView extends View {
    private int mFinalValue = -1;
    private int mCurrentValue = 0;
    private int mNextValue;
    private int mTextHeight;
    private float mTextCenterX;
    private float mAnimatedValue = 0;

    /*默认的文字大小*/
    private int mTextSize = sp2px(32);

    private Rect mTextBounds;
    private Paint mTextPaint;

    private static final int MESSAGE_WHAT = 1;
    private IFinishListener mListener;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_WHAT) {
                mAnimatedValue += 0.1;
                if (mAnimatedValue >= 1) {
                    mAnimatedValue = 0;
                    calculateValue(mCurrentValue + 1);
                    if (mCurrentValue == mFinalValue) {
                        invalidate();
                        if (mListener != null){
                            mListener.onFinish(WealthGodNumberView.this);
                        }
                        return;
                    }
                }
                invalidate();
                handler.sendEmptyMessageDelayed(MESSAGE_WHAT, 1);
            }
        }
    };

    public WealthGodNumberView(Context context) {
        this(context, null);
    }

    public WealthGodNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WealthGodNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextBounds = new Rect();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.parseColor("#422F19"));

        measureTextHeight();
    }

    public void setTextSize(int px) {
        mTextSize = sp2px(px);
        mTextPaint.setTextSize(mTextSize);

        measureTextHeight();

        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(width, height);

        mTextCenterX = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) >>> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, -mAnimatedValue * getMeasuredHeight());
        drawSelf(canvas);
        drawNext(canvas);
    }

    public void setFinalValue(int value) {
        mFinalValue = value;
    }

    public void start() {
        resetState();
        handler.sendEmptyMessage(MESSAGE_WHAT);
    }

    private void calculateValue(int number) {
        number = number == -1 ? 9 : number;
        number = number == 10 ? 0 : number;
        mCurrentValue = number;
        mNextValue = number + 1 == 10 ? 0 : number + 1;
    }

    private void drawNext(Canvas canvas) {
        float y = (float) (getMeasuredHeight() * 1.5);
        canvas.drawText(mNextValue + "", mTextCenterX, y + mTextHeight / 2, mTextPaint);
    }

    private void drawSelf(Canvas canvas) {
        int y = getMeasuredHeight() / 2;
        canvas.drawText(mCurrentValue + "", mTextCenterX, y + mTextHeight / 2, mTextPaint);
    }

    private void measureTextHeight() {
        mTextPaint.getTextBounds("0", 0, 1, mTextBounds);
        mTextHeight = mTextBounds.height();
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
                mTextPaint.getTextBounds("0", 0, 1, mTextBounds);
                value = mTextBounds.width();
                break;
        }
        return value + getPaddingLeft() + getPaddingRight();
    }

    private int measureHeight(int heightSpec) {
        int mode = MeasureSpec.getMode(heightSpec);
        int size = MeasureSpec.getSize(heightSpec);

        int value = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                value = size;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST://textSize
                mTextPaint.getTextBounds("0", 0, 1, mTextBounds);
                value = mTextBounds.height();
                break;
        }
        return value + getPaddingTop() + getPaddingBottom();
    }

    private int sp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }

    private void resetState() {
        mFinalValue = -1;
        mCurrentValue = 0;
    }

    public void setOnFinishListener(IFinishListener l){
        mListener = l;
    }

    public interface IFinishListener{
        void onFinish(WealthGodNumberView view);
    }
}
