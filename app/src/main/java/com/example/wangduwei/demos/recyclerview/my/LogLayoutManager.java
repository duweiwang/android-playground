package com.example.wangduwei.demos.recyclerview.my;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/3
 */

public class LogLayoutManager extends LinearLayoutManager {
    public LogLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        Log.d(RecyFragment.TAG, "LayoutManager::onLayoutChildren");
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        Log.d(RecyFragment.TAG, "LayoutManager::onAttachedToWindow");
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        Log.d(RecyFragment.TAG, "LayoutManager::onDetachedFromWindow");
    }

}
