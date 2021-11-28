package com.example.wangduwei.demos.recyclerview.pullrefreshloadrecyclerview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.wangduwei.demos.recyclerview.PullRefreshLoadRecyclerView;


/**
 * Created by linfaxin on 15/10/2.
 */
public class PullRefreshLoadGridActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PullRefreshLoadRecyclerView recyclerView = new PullRefreshLoadRecyclerView(this);
        setContentView(recyclerView);

        DemoAdapter adapter = new DemoAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 2));
    }
}
