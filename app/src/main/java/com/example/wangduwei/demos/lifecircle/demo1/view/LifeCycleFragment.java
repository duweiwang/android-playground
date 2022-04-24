package com.example.wangduwei.demos.lifecircle.demo1.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.AppState;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.lifecircle.demo1.data.Word;
import com.example.wangduwei.demos.lifecircle.demo1.viewmodel.WordListAdapter;
import com.example.wangduwei.demos.lifecircle.demo1.viewmodel.WordViewModel;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.util.List;

/**
 * @desc: 演示ViewModel  && LiveData  &&  LifeCycle的基本使用
 * @auther:duwei
 * @date:2018/11/8
 */
@PageInfo(description = "架构组件", navigationId = R.id.fragment_life_circle)
public class LifeCycleFragment extends BaseSupportFragment {
    private WordViewModel mWordViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lifecircle_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.floatbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this.getViewLifecycleOwner(), words -> {
            // Update the cached copy of the words in the adapter.
            adapter.setWords(words);
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(AppState.INSTANCE.getContext(), R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }
}
