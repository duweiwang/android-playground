package com.example.wangduwei.demos.recyclerview.layoutmanager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @desc:
 * @auther:duwei
 * @date:2017/12/6
 */

public class CustomLayoutManager extends RecyclerView.LayoutManager {


    @Override//唯一需要强制复写的方法
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
         return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
}
