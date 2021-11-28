package com.example.wangduwei.demos.view.scale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.wangduwei.demos.R;

/**
 * @desc: 利用手势实现对图片的缩放, 演示Matrix的mapRect
 * @auther:duwei
 * @date:2019/3/7
 */
public class GestureScaleImageView extends View implements ScaleGestureDetector.OnScaleGestureListener {

    private Bitmap mBitmap;
    private int mBitmapWidth, mBitmapHeight;

    private RectF mBitmapRect = new RectF();
    private Matrix mScaleMatrix = new Matrix();

    ScaleGestureDetector scaleGestureDetector;

    public GestureScaleImageView(Context context) {
        this(context, null);
    }

    public GestureScaleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.five_start_mark1);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();

        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
//        int action = event.getActionMasked();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                Log.d("wdw-scale", "down");
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN:
//                Log.d("wdw-scale", "pointer down");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.d("wdw-scale", "move");
//                break;
//            default:
//                break;
//        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);

        canvas.drawBitmap(mBitmap, null, mBitmapRect, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int finalWidth = 0, finalHeight = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST://wrap
                finalWidth = 300;
                break;
            case MeasureSpec.EXACTLY://match
                finalWidth = widthSize;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST://wrap
                finalHeight = 300;
                break;
            case MeasureSpec.EXACTLY://match
                finalHeight = heightSize;
                break;
        }

        setMeasuredDimension(finalWidth, finalHeight);
    }


    float last = 1;


    @Override
    public boolean onScale(ScaleGestureDetector detector) {//优化：缩放中心超出边界的话已边界为准
        float scaleFractor = detector.getScaleFactor();
        Log.d("wdw-scale", "CurrentSpan = " + detector.getCurrentSpan());
        Log.d("wdw-scale", "PreviousSpan = " + detector.getPreviousSpan());
        Log.d("wdw-scale", "scaleFractor = " + scaleFractor);

        float scaleX = detector.getFocusX();
        float scaleY = detector.getFocusY();

        mScaleMatrix.reset();
        //这里的参数传变化率，而不是变化的值
        mScaleMatrix.postScale(scaleFractor / last, scaleFractor / last, scaleX, scaleY);
        mScaleMatrix.mapRect(mBitmapRect);
        invalidate();
        last = scaleFractor;
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
