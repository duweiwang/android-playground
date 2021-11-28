package com.example.wangduwei.demos.recyclerview.my;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wangduwei.demos.R;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/3
 */

public class RecyViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    public RecyViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.item_tv);
    }
}
