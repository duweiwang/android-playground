package com.example.customview.basic_api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Xfermode混合模式演示
 */
public class XFermodeView extends View {
    private int mWidth = 400;
    private int mHeight = 400;

    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;

    private Paint mPaint;
    private Paint mTextPaint;

    public XFermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(20);

        mSrcBitmap = createSrcBitmap();
        mDstBitmap = createDstBitmap();
    }

    private Xfermode mXfermode;

    public XFermodeView(Context context) {
        this(context, null);
    }

    /**
     * 蓝色的圆
     */
    private Bitmap createDstBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(200, 200, 200, paint);
        return bitmap;
    }

    /**
     * 红色的矩形
     *
     * @return
     */
    private Bitmap createSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        canvas.drawRect(0, 0, 400, 400, paint);
        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(800, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GREEN);

        canvas.drawBitmap(mDstBitmap, 0, 0, null);
        canvas.drawText("DST", 200, 200, mTextPaint);

        canvas.drawBitmap(mSrcBitmap, 400, 0, null);
        canvas.drawText("SRC", 600, 200, mTextPaint);

        int id = canvas.saveLayer(0, 400, mWidth * 2, 1000, mPaint);
        canvas.drawBitmap(mDstBitmap, 0, 400, null);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mSrcBitmap, mWidth / 2, 600, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(id);
    }

    public void setXfermode(Xfermode xfermode) {
        mXfermode = xfermode;
        invalidate();
    }

}
