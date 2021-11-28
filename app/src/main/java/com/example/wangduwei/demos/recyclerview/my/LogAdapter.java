package com.example.wangduwei.demos.recyclerview.my;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangduwei.demos.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/3
 */

public class LogAdapter extends RecyclerView.Adapter<RecyViewHolder> {

    private Context context;
    private ArrayList<Integer> list;

    public LogAdapter(Context context, ArrayList<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(RecyFragment.TAG, "Adapter::onCreateViewHolder viewType = " + viewType);
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        RecyViewHolder viewHolder = null;
        if (viewType == 0) {
            view.setBackgroundColor(Color.RED);
            viewHolder = new RecyViewHolder(view);
        } else if (viewType == 1) {
            view.setBackgroundColor(Color.BLUE);
            viewHolder = new RecyViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyViewHolder holder, int position) {
        Log.d(RecyFragment.TAG, "Adapter::onBindViewHolder position = " + position);
        holder.textView.setText("position = " + position );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(RecyFragment.TAG, "Adapter::onAttachedToRecyclerView");
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d(RecyFragment.TAG, "Adapter::onDetachedFromRecyclerView");
    }

    @Override
    public void onViewAttachedToWindow(RecyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d(RecyFragment.TAG, "Adapter::onViewAttachedToWindow");
    }

    @Override
    public void onViewDetachedFromWindow(RecyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(RecyFragment.TAG, "Adapter::onViewDetachedFromWindow");
    }

    @Override
    public void onViewRecycled(RecyViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(RecyFragment.TAG, "Adapter::onViewRecycled");
    }

    @Override
    public boolean onFailedToRecycleView(RecyViewHolder holder) {
        Log.d(RecyFragment.TAG, "Adapter::onFailedToRecycleView");
        return super.onFailedToRecycleView(holder);
    }

    public void delete(int position) {
        if(position < 0 || position > getItemCount()) {
            return;
        }
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int fromPosition, int toPosition) {
//        Integer prev = list.remove(fromPosition);
//        list.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

}
