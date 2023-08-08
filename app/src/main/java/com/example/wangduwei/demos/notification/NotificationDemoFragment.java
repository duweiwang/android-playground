package com.example.wangduwei.demos.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.notification.basic.BasicNotificationUsage;
import com.example.wangduwei.demos.notification.style.NotificationStyle;
import com.example.wangduwei.demos.notification.style.NotificationStyleBigHeadupRemoteView;
import com.example.wangduwei.demos.notification.style.NotificationStyleBigPicture;
import com.example.wangduwei.demos.notification.style.NotificationStyleBigRemoteView;
import com.example.wangduwei.demos.notification.style.NotificationStyleBigText;
import com.example.wangduwei.demos.notification.style.NotificationStyleFullScreen;
import com.example.wangduwei.demos.notification.style.NotificationStyleInbox;
import com.example.wangduwei.demos.notification.style.NotificationStyleMessaging;
import com.example.wangduwei.demos.notification.style.NotificationStyleMessagingAction;
import com.example.wangduwei.demos.notification.style.NotificationStyleProgress;

/**
 * @desc: Notification相关
 * @auther:duwei
 * @date:2018/9/14
 */
@PageInfo(description = "演示安卓各种通知栏，大小图标，折叠等", navigationId = R.id.fragment_notification, title = "Notification", preview = R.drawable.preview_push_notification)
public class NotificationDemoFragment extends BaseSupportFragment implements View.OnClickListener {
    private NotificationStyle mStyle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notification, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.notification_container);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_basic:
                new BasicNotificationUsage(getActivity()).showNotification();
                return;
            case R.id.style_big_text:
                mStyle = new NotificationStyleBigText(getActivity());
                break;
            case R.id.style_big_picture:
                mStyle = new NotificationStyleBigPicture(getActivity());
                break;
            case R.id.style_full_screen:
                mStyle = new NotificationStyleFullScreen(getActivity());
                break;
            case R.id.style_progress:
                mStyle = new NotificationStyleProgress(getActivity());
                break;
            case R.id.style_message:
                mStyle = new NotificationStyleMessaging(getActivity());
                break;
            case R.id.style_message_action:
                mStyle = new NotificationStyleMessagingAction(getActivity());
                break;
            case R.id.style_inbox:
                mStyle = new NotificationStyleInbox(getActivity());
                break;
            case R.id.style_BigHeadupRemoteView:
                mStyle = new NotificationStyleBigHeadupRemoteView(getActivity());
                break;
            case R.id.style_BigRemoteView:
                mStyle = new NotificationStyleBigRemoteView(getActivity());
                break;
        }

        mStyle.showNotification(getActivity(), 5, System.currentTimeMillis());
    }
}
