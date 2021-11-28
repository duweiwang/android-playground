package com.example.wangduwei.demos.fragment.dynamic_manager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc:  演示了{FragmentTransaction#add  remove  replace} 的用法
 * @author: duwei
 * @date: 2017/12/5
 */
@PageInfo(description = "Fragment动态管理", navigationId = R.id.fragment_manager_add_replace_remove)
public class AddReplaceRemove extends BaseSupportFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_replace_remove, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_add_frag1).setOnClickListener(this);
        view.findViewById(R.id.btn_add_frag2).setOnClickListener(this);
        view.findViewById(R.id.btn_remove_frag2).setOnClickListener(this);
        view.findViewById(R.id.btn_repalce_frag1).setOnClickListener(this);
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }

    private void removeFragment2() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("fragment2");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * replace操作会清空之前add过的fragment
     */
    private void replaceFragment1() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment2 fragment2 = new Fragment2();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment2);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_frag1://点击三次同样会创建三个实例
                Fragment1 fragment1 = new Fragment1();
                addFragment(fragment1, "fragment1");
                break;
            case R.id.btn_add_frag2:
                Fragment2 fragment2 = new Fragment2();
                addFragment(fragment2, "fragment2");
                break;
            case R.id.btn_remove_frag2:
                removeFragment2();
                break;
            case R.id.btn_repalce_frag1:
                replaceFragment1();
                break;
        }
    }
}
