package com.example.wangduwei.demos.main;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lib_processor.FragmentInfo;
import com.example.wangduwei.demos.R;

import java.util.List;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/4/10
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private List<FragmentInfo> mList;
    private Context mContext;
    OnItemClickListener mOnItemClickListener;

    public MainAdapter(List<FragmentInfo> titles, Context context) {
        mList = titles;
        mContext = context;
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("wdw", "onCreateViewHolder");
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.main_recycler_item, null);
        MainViewHolder mainViewHolder = new MainViewHolder(itemView);
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.d("wdw", "onBindViewHolder");
        FragmentInfo obj = mList.get(position);
        holder.mItemName.setText(obj.getTitle());
        holder.mDesc.setText(obj.getDescription());
        if (obj.getPreview() != -1) {
            holder.mImage.setImageResource(obj.getPreview());
        } else {
            holder.mImage.setImageResource(android.R.color.transparent);
            holder.mImage.setBackgroundColor(mContext.getResources().getColor(R.color.md_orange_700));
        }
        holder.itemView.setOnClickListener(view -> mOnItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemName;
        private ImageView mImage;
        private TextView mDesc;

        public MainViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.entrance_flag);
            mItemName = itemView.findViewById(R.id.main_item_tv);
            mDesc = itemView.findViewById(R.id.entrace_desc);
        }


    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
