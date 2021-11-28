package com.example.customview.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @desc: 使用PorterDuffXfermode实现，先画上去的是DST，后画上去的是SRC
 * 使用SRC_IN模式，取圆角矩形和图片相交部分的SRC部分
 * @auther:duwei
 * @date:2018/8/30
 */

@SuppressLint("AppCompatCustomView")
public class RndCornerImg_PorterDuff extends ImageView {

    private Paint mPaint;
    private Xfermode mXfermode;
    private int mBorderRadius = 10;

    public RndCornerImg_PorterDuff(Context context) {
        this(context, null);
    }

    public RndCornerImg_PorterDuff(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RndCornerImg_PorterDuff(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //画源图像，为一个圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius, mBorderRadius,
                mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //画目标图像
        canvas.drawBitmap(drawableToBitamp(exChangeSize(getDrawable())), 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    /**
     * 图片拉升
     *
     * @param drawable
     * @return
     */
    private Drawable exChangeSize(Drawable drawable) {
        float scale = 1.0f;
        scale = Math.max(getWidth() * 1.0f / drawable.getIntrinsicWidth(), getHeight()
                * 1.0f / drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, (int) (scale * drawable.getIntrinsicWidth()),
                (int) (scale * drawable.getIntrinsicHeight()));
        return drawable;
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
