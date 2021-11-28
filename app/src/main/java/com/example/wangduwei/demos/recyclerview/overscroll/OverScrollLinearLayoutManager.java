package com.example.wangduwei.demos.recyclerview.overscroll;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

/**
 * Created by linfaxin on 15/8/29.
 * OverScroll For LinearLayout Manager
 */
public class OverScrollLinearLayoutManager extends LinearLayoutManager implements
        OverScrollImpl.OverScrollLayoutManager {
    OverScrollImpl overScrollImpl;

    public OverScrollLinearLayoutManager(RecyclerView recyclerView) {
        super(recyclerView.getContext());
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    public OverScrollLinearLayoutManager(RecyclerView recyclerView, int orientation, boolean reverseLayout) {
        super(recyclerView.getContext(), orientation, reverseLayout);
        overScrollImpl = new OverScrollImpl(recyclerView);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d("tag","OverScrollLinearLayoutManager::scrollVerticallyBy dy = "+ dy);
        return overScrollImpl.scrollVerticallyBy(this, dy, recycler, state);
    }

    @Override
    public int superScrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                       RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int getOverScrollDistance() {
        return overScrollImpl.getOverScrollDistance();
    }

    @Override
    public void setLockOverScrollTop(int topDistance) {
        overScrollImpl.setLockOverScrollTop(topDistance);
    }
}
