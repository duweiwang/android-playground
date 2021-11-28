package com.example.wangduwei.demos.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.IMyAidlInterface;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.service.communicate.ComService;
import com.example.wangduwei.demos.service.remote.RemoteService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * @desc: 演示Service跑在其他进程，通过AIDL进行通信
 * 演示Service跑在本进程，通过继承Binder进行通讯
 * @auther:duwei
 * @date:2018/5/12
 */
@PageInfo(description = "AIDL通信", navigationId = R.id.fragment_use_aidl)
public class AidlFragment extends BaseSupportFragment implements Handler.Callback {

    private IMyAidlInterface iMyAidlInterface;
    private Button textView, textView1, textView2;
    private TextView mInfoText;
    private ComService.MyBinder myBinder;
    private StringBuilder stringBuilder = new StringBuilder();

    private void append(String string) {
        stringBuilder.append(string);
        stringBuilder.append("\n");
        mInfoText.setText(stringBuilder.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        textView = new Button(getActivity());
        textView.setText("绑定服务");
        textView.setOnClickListener(this);

        textView1 = new Button(getActivity());
        textView1.setText("获取名字");
        textView1.setOnClickListener(this);

        textView2 = new Button(getActivity());
        textView2.setText("绑定");
        textView2.setOnClickListener(this);

        mInfoText = new TextView(getActivity());
        mInfoText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        mInfoText.setTextColor(Color.BLACK);

        linearLayout.addView(textView, lp);
        linearLayout.addView(textView1, lp);
        linearLayout.addView(mInfoText, lp);
        return linearLayout;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(textView)) {
            getActivity().bindService(new Intent(getActivity(), RemoteService.class), new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d("demo", "onServiceConnected，Thread = " + Thread.currentThread().getName());
                    iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.d("demo", "onServiceDisconnected");
                }
            }, BIND_AUTO_CREATE);
        } else if (view.equals(textView1)) {
            try {
                Toast.makeText(getActivity(), iMyAidlInterface.getName(), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (view.equals(textView2)) {
            Intent intent = new Intent(getActivity(), ComService.class);
            getActivity().bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    myBinder = (ComService.MyBinder) service;
                    myBinder.communicate();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, BIND_AUTO_CREATE);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
