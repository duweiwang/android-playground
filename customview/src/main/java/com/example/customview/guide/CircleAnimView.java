package com.example.customview.guide;

import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 简易圆形波纹动画控件
 */
public class CircleAnimView extends View {
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private int mMinSize;
    private float mRadius;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private int mBigCircleColor = 0x4cFFFFFF;
    private int mSmallCircleColor = 0x77FFFFFF;
    public CircleAnimView(Context context) {
        this(context,null);
    }

    public CircleAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w != oldw || h != oldh){
            mWidth = w;
            mHeight = h;
            mMinSize = mWidth>mHeight?mHeight:mWidth;
        }
    }

    public void startAnim(){
        if(mValueAnimator!=null&&mValueAnimator.isRunning()){
            return;
        }
        Keyframe k1 = Keyframe.ofFloat(0,mMinSize/2);
        Keyframe k2 = Keyframe.ofFloat(0.5f,mMinSize/4);
        Keyframe k3 = Keyframe.ofFloat(1,mMinSize/2);
        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("scale",k1,k2,k3);
        mValueAnimator = ValueAnimator.ofPropertyValuesHolder(propertyValuesHolder);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(2000);
        mValueAnimator.start();
    }

    /**
     * 因为这个动画会一直重复执行，没用时记得停止动画
     */
    public void stopAnim(){
        if(mValueAnimator!=null&&mValueAnimator.isRunning()) {
            Log.d("hyh","CircleAnimView: stopAnim: ");
            mValueAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mBigCircleColor);
        canvas.drawCircle(mWidth/2,mHeight/2,mRadius,mPaint);
        mPaint.setColor(mSmallCircleColor);
        canvas.drawCircle(mWidth/2,mHeight/2,mMinSize/4,mPaint);
    }
}
