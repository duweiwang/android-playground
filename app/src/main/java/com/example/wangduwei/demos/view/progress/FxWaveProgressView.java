package com.example.wangduwei.demos.view.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;


import com.example.customview.utils.SystemUtils;
import com.example.wangduwei.demos.R;

import java.lang.ref.WeakReference;
import java.util.Locale;

import static android.util.TypedValue.applyDimension;

public class FxWaveProgressView extends View {
    private static final String TAG = FxWaveProgressView.class.getSimpleName();
    public static final int WAVE_GAP = 40;
    private static final int SECOND_FACTOR = 60;//60s
    public static final int MAX_WAVES= 2;
    public static final int DEFAULT_TEXT_SIZE = 14;
    public static final String DEFAULT_TEXT = "99:99";
    public static final float SQRT_DOUBLE = (float) Math.sqrt(2);

    private float mOuterRadius;//最大半径
    private float mInnerRadius;//最小半径
    private int mProgressColor;//进度条色值
    private float mProgressWidth;//进度条宽度
    private float mProgress;//当前进度
    private Paint mProgressPaint;//进度条画笔
    private Paint mWavePaint;//波纹扩散画笔
    private Paint mTextPaint;//倒计时文本画笔
    private int mWaveStartAlpha;//波纹开始时的alpha值
    private float mTextSize;//计时显示字体大小
    private int mTextColor;//字体色值
    private String mText;//倒计时显示
    private Rect mBound;//用来计算文字的宽高
    private RectF mArcRectF;//用来计算圆弧的位置
    private FxWaveCountDownTimer mCountDownTimer;//用来处理倒计时
    private Interpolator mInterpolator;
    private int mMaxWaves = MAX_WAVES;

    private float mCenterX;
    private float mCenterY;

    private boolean isCountStarting = true;

    private int mDesiredSize;
    private float mDefaultInnerR;
    private float mDefaultOuterR;
    private float max_size;

    private OnTickFinishListener mOnTickFinishListener;


    public FxWaveProgressView(Context context) {
        super(context);
        init(context);
    }

    public FxWaveProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FxWaveProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FxWaveProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        float width = SystemUtils.getDisplayWidth(context);
        float height = SystemUtils.getDisplayHeight(context);
        max_size = Math.min(width,height);
        mProgressWidth = SystemUtils.dip2px(context,1);//默认进度条宽度
        Rect size = getDefaultSize();
        int innerW = size.width();
        mInnerRadius = mDefaultInnerR = innerW / SQRT_DOUBLE - mProgressWidth;
        mOuterRadius = mDefaultOuterR = 2 / SQRT_DOUBLE * (mDefaultInnerR + WAVE_GAP) - mProgressWidth;
        mDesiredSize = (int) (2 * mDefaultOuterR);
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
//        mWavePaint.setStyle(Paint.Style.STROKE);
        mProgressColor = getResources().getColor(R.color.fa_wave_progress_color);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextSize= DEFAULT_TEXT_SIZE;//默认字体大小
        mTextColor = mProgressColor;

        mBound = new Rect();
        mArcRectF = new RectF();
        mWavePaint.setColor(mProgressColor);
        mInterpolator = new FastOutSlowInInterpolator();
        mWaveStartAlpha = 76;


    }

    private float getPaintTextSize(float textSize) {
        float paintSize =  applyDimension(
                TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        return paintSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;
        }else {
            width = mDesiredSize;

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(widthSize, width);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize;
        }else{
            height = mDesiredSize;

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        int size = Math.max(width,height);
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getWidth() - getPaddingRight() - getPaddingLeft();
        int height = getHeight() - getPaddingBottom() - getPaddingTop();
        mCenterX = (width/2.0f);
        mCenterY = height/2.0f;
        mOuterRadius = width/2f;
        if(mInnerRadius < mDefaultInnerR){
            mInnerRadius = mDefaultInnerR;
        }
        if(mOuterRadius < mDefaultOuterR){
            mOuterRadius = mDefaultOuterR;
        }

    }


    int count = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWaves(canvas);
        drawCenterBackground(canvas);
        drawCircleProgress(canvas);
        drawCountDownText(canvas);



    }

    private void drawWaves(Canvas canvas) {
        if(canvas == null){
            return;
        }
        float detaRadius = mOuterRadius - mInnerRadius;
        if(isCountStarting) {
            float waveInternal = detaRadius / mMaxWaves;
            for (int step = count; step <= detaRadius; step += waveInternal) {
                mWavePaint.setColor(mProgressColor);
                //step越大越靠外就越透明
                float percent = (1.0f - (step / (detaRadius)));
                percent = mInterpolator.getInterpolation(percent);
                mWavePaint.setAlpha((int) (mWaveStartAlpha * percent));
                canvas.drawCircle(mCenterX, mCenterY, (mInnerRadius + (1 - percent) * (detaRadius)), mWavePaint);
            }

            count += 2;//波扩散的幅度
            count %= waveInternal;
        }else{
            float percent = 0.5f;
            mWavePaint.setColor(mProgressColor);
            mWavePaint.setAlpha((int) (mWaveStartAlpha * percent));
            canvas.drawCircle(mCenterX, mCenterY, (mInnerRadius + (1 - percent) * (detaRadius)), mWavePaint);
        }
        canvas.save();
    }

    private void drawCenterBackground(Canvas canvas) {
        if(canvas == null){
            return;
        }
        canvas.restore();
        mWavePaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX,mCenterY,mInnerRadius+mProgressWidth/2,mWavePaint);
    }

    private void drawCircleProgress(Canvas canvas) {
        if(canvas == null){
            return;
        }
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mArcRectF.left = mCenterX - mInnerRadius;
        mArcRectF.right = mCenterX + mInnerRadius;
        mArcRectF.top = mCenterY - mInnerRadius;
        mArcRectF.bottom = mCenterY + mInnerRadius;
        canvas.rotate(-90,mCenterX,mCenterY);
        float angle = 360f * (1f-mProgress);
        canvas.drawArc(mArcRectF,0,angle,false,mProgressPaint);
        canvas.save();
        canvas.restore();
        canvas.rotate(90,mCenterX,mCenterY);
    }

    private void drawCountDownText(Canvas canvas) {
        if(!TextUtils.isEmpty(mText) && canvas != null) {
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(getPaintTextSize(mTextSize));

            mTextPaint.getTextBounds(mText, 0, mText.length(), mBound);

            float textX = mCenterX - mBound.width() / 2.0f;
            float textY = mCenterY + mBound.height() / 2.0f;
            canvas.drawText(mText, textX, textY, mTextPaint);
            canvas.save();
            canvas.restore();
        }
    }

    private Rect getDefaultSize() {
        Paint paint = new Paint();
        float defaultSize = getPaintTextSize(DEFAULT_TEXT_SIZE);//默认字体大小
        paint.setTextSize(defaultSize);
        Rect bound = new Rect();
        paint.getTextBounds(DEFAULT_TEXT,0,DEFAULT_TEXT.length(),bound);
        return bound;
    }


    /**
     *
     * @param time
     * @param milliInternal
     */
    public void startCountDown(int time,long milliInternal,int totalTime){
        if(time<=0){
            return;
        }
        long millionTime = time * 1000;
        mCountDownTimer = new FxWaveCountDownTimer(this,millionTime,milliInternal,totalTime);
        mCountDownTimer.cancel();
        mCountDownTimer.start();
        handleCountDown(millionTime);
    }

    public void stopCountDown(){
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountDown();
        mOnTickFinishListener = null;
    }

    private void handleCountDown(long millisUntilFinished){
        int time = (int) (millisUntilFinished / 1000);
        int second  = time % SECOND_FACTOR;
        int min = time / SECOND_FACTOR;
        int hour = min / SECOND_FACTOR;
        if(hour > 0) {
            mText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, min, second);
        }else{
            mText = String.format(Locale.getDefault(), "%02d:%02d", min, second);
        }

        postInvalidate();
        if (mOnTickFinishListener != null) {
            mOnTickFinishListener.onTick(millisUntilFinished);
        }
    }


    private void handleTickFinished(){
        isCountStarting = false;
        if (mOnTickFinishListener != null) {
            mOnTickFinishListener.onTickFinish();
        }
    }

    public OnTickFinishListener getOnTickFinishListener() {
        return mOnTickFinishListener;
    }

    public void setOnTickFinishListener(OnTickFinishListener onTickFinishListener) {
        mOnTickFinishListener = onTickFinishListener;
    }

    public float getOuterRadius() {
        return mOuterRadius;
    }

    public void setOuterRadius(float outerRadius) {
        mOuterRadius = outerRadius;
        postInvalidate();
    }

    public float getInnerRadius() {
        return mInnerRadius;
    }

    public void setInnerRadius(float innerRadius) {
        mInnerRadius = innerRadius;
        postInvalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        if(progressColor != mProgressColor) {
            mProgressColor = progressColor;
            postInvalidate();
        }
    }

    public float getProgressWidth() {
        return mProgressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        progressWidth = SystemUtils.dip2px(getContext(),progressWidth);
        if(progressWidth != mProgressWidth && progressWidth >0) {
            mProgressWidth = progressWidth;
            postInvalidate();
        }
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if(progress < 0 ){
            progress = 0;
        }

        mProgress = progress;
        postInvalidate();
    }

    public int getMaxWaves() {
        return mMaxWaves;
    }

    public void setMaxWaves(int maxWaves) {
        mMaxWaves = maxWaves;
        postInvalidate();
    }


    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        if(textSize > 0){
            mTextSize = findTextSize(textSize);
            postInvalidate();
        }

    }

    private float findTextSize(float textSize){
        if(textSize < DEFAULT_TEXT_SIZE){
            return findTextSize(DEFAULT_TEXT_SIZE);
        }
        float ts = getPaintTextSize(textSize);
        mTextPaint.setTextSize(ts);
        mTextPaint.getTextBounds(DEFAULT_TEXT,0,DEFAULT_TEXT.length(),mBound);
        float bound = Math.max(mBound.width(),mBound.height());
        mInnerRadius = bound / SQRT_DOUBLE - mProgressWidth;
        mOuterRadius = 2 / SQRT_DOUBLE * (mInnerRadius + WAVE_GAP) - mProgressWidth;
        mDesiredSize = (int) (2 * mOuterRadius);

        if(mDesiredSize > max_size){
            textSize = (textSize + DEFAULT_TEXT_SIZE)/2;
            return findTextSize(textSize);
        }else{
            return textSize;
        }

    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        postInvalidate();
    }

    static class FxWaveCountDownTimer extends CountDownTimer {
        private WeakReference<FxWaveProgressView> mReference;
        private long millisTotalTime;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public FxWaveCountDownTimer(FxWaveProgressView view, long millisInFuture, long countDownInterval,long totalTime) {
            super(millisInFuture, countDownInterval);
            mReference = new WeakReference<>(view);
            this.millisTotalTime = totalTime * 1000;
        }

        @Override
        public void onTick(long millisUntilFinished) {

            FxWaveProgressView view = mReference.get();
            float progress = millisUntilFinished * 1.0f / millisTotalTime;
            if(view!=null){
                view.handleCountDown(millisUntilFinished);
                view.setProgress(progress);
            }

        }

        @Override
        public void onFinish() {
            FxWaveProgressView view = mReference.get();
            if(view!=null){
                view.setProgress(0f);
                view.handleCountDown(0);
                view.handleTickFinished();
            }
        }
    }


}
