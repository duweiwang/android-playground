package com.example.wangduwei.demos.recyclerview.my;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/6
 */

public class RecyItemTouchCallback extends ItemTouchHelper.Callback {
    private LogAdapter mAdapter;

    public RecyItemTouchCallback(LogAdapter adapter) {
        this.mAdapter = adapter;
    }

    /**
     * 返回一个复合标志位：该标志位定义了在idle, swiping, dragging各状态下的移动方向是否可用
     * 可用以下方法产生该值{@link #makeMovementFlags(int, int)}or {@link #makeFlag(int, int)}.
     * 复合值由三个8位组成：第一个8位代表IDLE状态，中8位代表SWIPE状态，低8位代表DRAG状态。每个8位可用通过
     * “|”运算符组合
     * <p>
     * 例如：如果你想允许左右swiping，但是只能从右边开始滑动，你可以这样写：
     * <pre>  makeFlag(ACTION_STATE_IDLE, RIGHT) | makeFlag(ACTION_STATE_SWIPE, LEFT | RIGHT);  </pre>
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//      //支持上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        //支持左右滑动
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        int flags = makeMovementFlags(dragFlags, swipeFlag);
        return flags;
    }

    /**
     * 当被拖拽的item从旧位置移到新位置的过程中回调
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Called when a ViewHolder is swiped by the user.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.delete(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//default true
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();//default true
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //  item被选中的操作
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//            viewHolder.itemView.setBackgroundResource(android.R.color.darker_gray);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 移动过程中重新绘制Item，随着滑动的距离，设置Item的透明度
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float x = Math.abs(dX) + 0.5f;
        float width = viewHolder.itemView.getWidth();
        float alpha = 1f - x / width;
        viewHolder.itemView.setAlpha(alpha);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive);
    }

    /**
     * 用户操作完毕或者动画完毕后调用，恢复item的背景和透明度
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 操作完毕后恢复颜色
//        viewHolder.itemView.setBackgroundResource(R.color.md_white);
        viewHolder.itemView.setAlpha(1.0f);
        super.clearView(recyclerView, viewHolder);
    }

}
