package com.example.wangduwei.demos.recyclerview.pullrefreshloadrecyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.recyclerview.overscroll.OverScrollLinearLayoutManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@PageInfo(description = "RecyclerView",navigationId = R.id.fragment_recycler_pull)
public class RecyclerDemoFragment extends BaseSupportFragment {
    static LinkedHashMap<String, Class<? extends Activity>> data = new LinkedHashMap<>();
    static{
        data.put("1.Pull to refresh", PullToRefreshActivity.class);
        data.put("2.Pull refresh and load", PullRefreshLoadActivity.class);
        data.put("3.Pull refresh and load grid", PullRefreshLoadGridActivity.class);
        data.put("", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_demo,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView =view. findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new OverScrollLinearLayoutManager(recyclerView));
        recyclerView.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(getActivity());
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                String key = new ArrayList<>(data.keySet()).get(position);
                holder.button.setText(key);
                final Class<? extends Activity> value = data.get(key);
                if(value!=null){
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), value));
                        }
                    });
                }
            }


            @Override
            public int getItemCount() {
                return data.size();
            }
        });
    }

    class ViewHolder extends DemoViewHolder{
        public ViewHolder(Context context) {
            super(context);
            button.setMinHeight(getResources().getDisplayMetrics().heightPixels / data.size());
        }
    }
}
