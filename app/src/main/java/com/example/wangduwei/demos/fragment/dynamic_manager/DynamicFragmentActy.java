package com.example.wangduwei.demos.fragment.dynamic_manager;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.example.wangduwei.demos.R;

/**
 * @desc:
 * @auther:duwei
 * @date:2017/12/5
 */

/***
 * 使用support包必须继承{@link FragmentActivity}
 *
 * <p>当多次点击按钮后，通过{@code getSupportFragmentManager#getFragments}方法，
 * 查看内存有多个fragment实例</>
 */
public class DynamicFragmentActy extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_fragment_acty);

        Button btnLoadFrag1 = findViewById(R.id.btn_show_fragment1);
        btnLoadFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /***
                 * 获取到FragmentManager，在V4包中通过getSupportFragmentManager，
                 * 在系统中原生的Fragment是通过getFragmentManager获得的
                 */
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment1 fragment1 = new Fragment1();
                transaction.add(R.id.fragment_container, fragment1);
                transaction.commit();
            }
        });

        Button btnLoagFrag2 = findViewById(R.id.btn_show_fragment2);
        btnLoagFrag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment2 fragment2 = new Fragment2();
                transaction.add(R.id.fragment_container, fragment2);
                transaction.commit();
            }
        });
    }
}
