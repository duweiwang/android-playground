package com.example.wangduwei.demos.fragment.stack;

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
import com.example.wangduwei.demos.fragment.dynamic_manager.Fragment1;
import com.example.wangduwei.demos.fragment.dynamic_manager.Fragment2;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc: 演示Fragment的回退栈。
 * {@link FragmentTransaction#addToBackStack(String)}传入Fragment在栈中的名字。
 * {@link FragmentManager#popBackStack(String, int)} 中第二个参数的意义区分：
 * <p>
 * The {@link FragmentManager#POP_BACK_STACK_INCLUSIVE} flag can be used to control whether
 * the named state itself is popped. If null, only the top state is popped.
 * flags Either 0 or {@link FragmentManager#POP_BACK_STACK_INCLUSIVE}.
 * @author :duwei
 * @date: 2017/12/5
 */
@PageInfo(description = "Fragment的回退栈", navigationId = R.id.fragment_manager_stack)
public class FragmentStackTest extends BaseSupportFragment implements View.OnClickListener,
        FragmentManager.OnBackStackChangedListener {
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add1).setOnClickListener(this);
        view.findViewById(R.id.add2).setOnClickListener(this);
        view.findViewById(R.id.add3).setOnClickListener(this);
        view.findViewById(R.id.add4).setOnClickListener(this);
        view.findViewById(R.id.popbackStack).setOnClickListener(this);
        view.findViewById(R.id.backto2_0).setOnClickListener(this);
        view.findViewById(R.id.backto2inclusive).setOnClickListener(this);
    }

    private int stackID1, stackID2, stackID3, stackID4;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add1:
                Fragment1 fragment1 = new Fragment1();
                stackID1 = addFragment(fragment1, "fragment1");
                break;
            case R.id.add2:
                Fragment2 fragment2 = new Fragment2();
                stackID2 = addFragment(fragment2, "fragment2");
                break;
            case R.id.add3:
                Fragment3 fragment3 = new Fragment3();
                stackID3 = addFragment(fragment3, "fragment3");
                break;
            case R.id.add4:
                Fragment4 fragment4 = new Fragment4();
                stackID4 = addFragment(fragment4, "fragment4");
                break;
            case R.id.popbackStack:
                popBackStack();
                break;
            case R.id.backto2_0:
                popBackStackToFrag2_0();
                break;
            case R.id.backto2inclusive:
                popBackStackToFrag2_Inclusive();
                break;
        }
    }

    private int addFragment(Fragment fragment, String stackName) {
        FragmentManager manager = fragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.addToBackStack(stackName);
        return transaction.commit();
    }

    private void popBackStack() {
        fragmentManager.popBackStack();
    }

    private void popBackStackToFrag2_0() {
        fragmentManager.popBackStack("fragment2", 0);//方法一,通过TAG回退
        // manager.popBackStack(stackID2,0);//方法二,通过Transaction ID回退
    }

    private void popBackStackToFrag2_Inclusive() {
        fragmentManager.popBackStack("fragment2", FragmentManager.POP_BACK_STACK_INCLUSIVE);//方法一,通过TAG回退
// manager.popBackStack(stackID2,FragmentManager.POP_BACK_STACK_INCLUSIVE);//方法二,通过Transaction ID回退
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentManager.removeOnBackStackChangedListener(this);
    }
}
