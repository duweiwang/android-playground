package com.example.wangduwei.demos.ipc.pool;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.ICompute;
import com.example.wangduwei.demos.ISecurityCenter;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/7/27
 */
@PageInfo(description = "Binder管理池", navigationId = R.id.fragment_binder_pool)
public class BinderPoolDemoFragment extends BaseSupportFragment {
    private static final String TAG = "BinderPoolActivity";
    private TextView mInfo;
    private StringBuilder sb = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_biner_pool, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.start_btn).setOnClickListener(this);
        mInfo = view.findViewById(R.id.text_info);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.start_btn) {
            append("启动任务");
            new Thread(() -> doWork()).start();
        }
    }

    private void append(String str) {
        sb.append(str + "\n");
        getActivity().runOnUiThread(() -> mInfo.setText(sb.toString()));

    }

    private void doWork() {
        BinderPoolManager binderPoolManager = BinderPoolManager.getInstance(getActivity());
        IBinder securityBinder = binderPoolManager.queryBinder(BinderPoolService.BINDER_SECURITY_CENTER);
        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        String msg = "helloworld-安卓";
        try {
            append("加密中...");
            String encrypt = iSecurityCenter.encrypt(msg);
            append("加密结果 = " + encrypt);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------------------------------------------
        IBinder computeBinder = binderPoolManager.queryBinder(BinderPoolService.BINDER_COMPUTE);
        ICompute iCompute = ComputeImpl.asInterface(computeBinder);
        try {
            append("计算中...");
            long start = System.currentTimeMillis();
            int result = iCompute.add(1, 2);
            long spend = (System.currentTimeMillis() - start) / 1000;
            append("计算结果 = " + result);
            append("耗时 = " + spend + "s");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
