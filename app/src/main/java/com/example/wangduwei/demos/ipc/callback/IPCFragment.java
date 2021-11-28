package com.example.wangduwei.demos.ipc.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
 * @desc: 跨进程通讯和回调，如何实现良好封装？？
 * 参考FileDownloader的跨进程实现
 * <p>
 * 回调接口需要单独的AIDL{@link com.example.wangduwei.demos.ICallback}
 * 业务逻辑需要单独的AIDL{@link com.example.wangduwei.demos.IRemoteService}
 * @author: duwei
 * @date: 2018/8/23
 */
@PageInfo(description = "跨进程回调", navigationId = R.id.fragment_ipc_test)
public class IPCFragment extends BaseSupportFragment implements BaseIPCWithCallback.StateListener {
    //实现ServiceConnection接口
    private IPCClientClass mClient = new IPCClientClass();
    private TextView mTextView;

    private static final int MESSAGE_WHAT = 1;
    private static StringBuilder stringBuilder = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ipc_callback, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ipc_bind_service).setOnClickListener(this);
        mTextView = view.findViewById(R.id.ipc_info);
        mClient.setListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ipc_bind_service:
                bindRemoteService();
                break;
        }
    }

    private void bindRemoteService(){
        Intent intent = new Intent(getActivity(), RemoteService.class);
        getActivity().bindService(intent, mClient, Activity.BIND_AUTO_CREATE);
    }

    private void append(String string) {
        stringBuilder.append(string);
        stringBuilder.append("\n");
        Message message = Message.obtain();
        message.what = MESSAGE_WHAT;
        message.obj = stringBuilder.toString();
    }

    @Override
    public void onMessage(String msg) {
        append(msg);
        mTextView.setText(stringBuilder.toString());
    }
}
