package com.example.wangduwei.demos.jni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc: 1. 从C返回字符串
 * 2.C操作Java数组
 * @author: duwei
 * @date: 2019/2/27
 */
@PageInfo(description = "JNI基础", navigationId = R.id.fragment_jni)
public class JniDemoFragment extends BaseSupportFragment {
    int[] ar = new int[]{1, 2, 3, 4, 5};
    int[] array = new int[]{1, 2, 3, 4, 5,6,7,8,9,0};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_jnidemo, null);
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JniCenter jniCenter = new JniCenter();

        //方法操作：访问静态方法和实例方法
        StringBuilder sb = new StringBuilder();
        sb.append("getFromC = " + JniCenter.getStringFromC());
        sb.append("\n");
        sb.append("getFromC-Statistic = " + jniCenter.getStringFromCObjectMethod());
        sb.append("\n");


        //数组操作：
        jniCenter.changeArray(ar);
        sb.append("changeArray = ");
        for (int i : ar) {
            sb.append(i + ",");
        }
        sb.append("\n");


        //变量操作：访问java的静态变量和成员变量
        jniCenter.accessJavaField();
        sb.append("accessJavaField = " + jniCenter.getStr());
        sb.append("\n");
        jniCenter.accessJavaField2();
        sb.append("accessJavaField2 = " + jniCenter.getStr());
        sb.append("\n");
        jniCenter.accessJavaStatisticField();
        sb.append("accessJavaStatisticField = " + JniCenter.getsNum());
        sb.append("\n");

        //数组操作:求和
        int result = jniCenter.sumArray(array);
        sb.append("sum = " + result);
        sb.append("\n");
        //native初始化数组
        int[][] arrar = JniCenter.initArrayInNative(5);
        sb.append("\nvalue of array:\n");
        for (int i = 0; i < arrar.length; i++) {
            int array[] = arrar[i];
            for (int j = 0; j < array.length; j++) {
                sb.append(array[j] + ",");
            }
            sb.append("\n");
        }

        ((TextView) view.findViewById(R.id.get_from_c)).setText(sb.toString());
    }
}
