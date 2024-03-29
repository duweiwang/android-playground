package com.example.wangduwei.demos.recyclerview.pullrefreshloadrecyclerview;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by linfaxin on 15/10/2.
 */
public class DemoDecoration extends RecyclerView.ItemDecoration{
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int offset = (int) (12 * view.getContext().getResources().getDisplayMetrics().density);
        outRect.set(offset, offset, offset, offset);
    }
}
