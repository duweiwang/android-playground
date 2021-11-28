package com.example.wangduwei.demos.recyclerview.my;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @desc: 带日志的RecyclerView
 * @auther:duwei
 * @date:2018/9/4
 */

public class LogRecyclerView extends RecyclerView {
    public LogRecyclerView(Context context) {
        super(context);
    }

    public LogRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LogRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(RecyFragment.TAG, "LogRecyclerView::onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        Log.d(RecyFragment.TAG, "before onMeasure mAttachedScrap.size = " + RecyFragment.mAttachedScrap.size());
        super.onMeasure(widthSpec, heightSpec);
        Log.d(RecyFragment.TAG, "after onMeasure mAttachedScrap.size = " + RecyFragment.mAttachedScrap.size());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(RecyFragment.TAG, "LogRecyclerView::onSizeChanged");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(RecyFragment.TAG, "LogRecyclerView::onLayout");
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d(RecyFragment.TAG, "LogRecyclerView::onScrollChanged");
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
//        if (getScrollState() == SCROLL_STATE_IDLE) {
            Log.d(RecyFragment.TAG, "mAttachedScrap.size = " + RecyFragment.mAttachedScrap.size());
            Log.d(RecyFragment.TAG, "mCachedViews.size = " + RecyFragment.mCachedViews.size());
//        }
//        Log.d(RecyFragment.TAG, "LogRecyclerView::onScrolled");
    }
}
