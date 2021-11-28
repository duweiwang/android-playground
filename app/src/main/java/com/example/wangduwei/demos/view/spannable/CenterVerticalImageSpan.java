package com.example.wangduwei.demos.view.spannable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

import java.lang.ref.WeakReference;

public class CenterVerticalImageSpan extends ClickableImageSpan {

    public CenterVerticalImageSpan(Drawable d) {
        super(d);
    }
    public CenterVerticalImageSpan(Context context, int rerId) {
        super(context,rerId);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getCachedDrawable();
        if (drawable instanceof BitmapDrawable) {
            // 这里的实现方案待查
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (null == bitmap || bitmap.isRecycled()) {
                return;
            }
        }
        canvas.save();

        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        float textHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        float imgHeight = drawable.getBounds().bottom - drawable.getBounds().top;

        float transY = (float) fontMetricsInt.bottom + (float) y - (textHeight / 2) - (imgHeight / 2);
        if (transY < 0) {
            transY = 0;
        }
        canvas.translate(x, transY);

        drawable.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
