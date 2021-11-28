package com.example.customview.floatview.impl3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.R;


/**
 * <p> 圆圈倒计时View
 *
 * @auther : wangduwei
 * @since : 2019/8/13  16:35
 **/
public class CircleCountDownView extends View {
    private boolean isHalfe = true;
    private Paint mBgPaint;
    private Bitmap mBitmap;
    private RectF mRectF = new RectF();
    private RectF mBgRecF = new RectF();


    private static final String DEFAULT_COLOR_OUTTER_BG = "#66D309FF";

    public static final float DEFAULT_OUTTER_RADIUS_DP = 20f;//84/4=21
    public static final int DEFAULT_INNER_RADIUS_DP = 18;//76/4=19


    public CircleCountDownView(Context context) {
        this(context, null);
    }

    public CircleCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.parseColor(DEFAULT_COLOR_OUTTER_BG));
        mBgPaint.setStyle(Paint.Style.FILL);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_game_float_bg);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(
                w / 2 - dp2px(DEFAULT_INNER_RADIUS_DP),
                h / 2 - dp2px(DEFAULT_INNER_RADIUS_DP),
                w / 2 + dp2px(DEFAULT_INNER_RADIUS_DP),
                h / 2 + dp2px(DEFAULT_INNER_RADIUS_DP)
        );
        mBgRecF.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawOutterBg(canvas);
        drawBitmap(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureWidth(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    public void setHalfe(boolean isHalfe) {
        this.isHalfe = isHalfe;
        invalidate();
    }

    private void drawOutterBg(Canvas canvas) {
        canvas.drawArc(mBgRecF, -90, isHalfe ? -180 : -360, false, mBgPaint);
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, mRectF, null);
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
