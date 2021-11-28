package com.example.wangduwei.demos.view.spannable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * @auther: davewang
 * @since: 2019/7/19
 **/
public abstract class ClickableImageSpan extends ImageSpan {

    public ClickableImageSpan(@NonNull Context context, @NonNull Bitmap bitmap) {
        super(context, bitmap);
    }

    public ClickableImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
    }

    public ClickableImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    public ClickableImageSpan(@NonNull Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public ClickableImageSpan(@NonNull Drawable drawable, @NonNull String source) {
        super(drawable, source);
    }

    public ClickableImageSpan(@NonNull Drawable drawable, @NonNull String source, int verticalAlignment) {
        super(drawable, source, verticalAlignment);
    }

    public ClickableImageSpan(@NonNull Context context, @NonNull Uri uri) {
        super(context, uri);
    }

    public ClickableImageSpan(@NonNull Context context, @NonNull Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public ClickableImageSpan(@NonNull Context context, int resourceId) {
        super(context, resourceId);
    }

    public ClickableImageSpan(@NonNull Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public abstract void onClick(View view);

}
