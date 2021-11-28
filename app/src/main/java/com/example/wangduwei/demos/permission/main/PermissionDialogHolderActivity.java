package com.example.wangduwei.demos.permission.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.wangduwei.demos.AppState;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/2
 */

public class PermissionDialogHolderActivity extends Activity {
    private static final String EXTRA_PERMISSION_NAME = "extra_permission_name";
    private static final String EXTRA_PERMISSION_GROUP_NAME = "extra_permission_group_name";
    private static final String PERMISSION_NAME = "PERMISSION_NAME";
    private static final int REQUEST_CODE_RQST_PERMISSION = 999;
    private String name;


    public static void start(String permission) {
        Intent intent = new Intent(AppState.INSTANCE.getContext(), PermissionDialogHolderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_PERMISSION_NAME, permission);
        AppState.INSTANCE.getContext().startActivity(intent);
    }

    public static void start(String name, String[] permissions) {
        Intent intent = new Intent(AppState.INSTANCE.getContext(), PermissionDialogHolderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PERMISSION_NAME, name);
        intent.putExtra(EXTRA_PERMISSION_GROUP_NAME, permissions);
        AppState.INSTANCE.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        //优先判断多个权限
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSION_GROUP_NAME);
        name = getIntent().getStringExtra(PERMISSION_NAME);
        if (permissions != null && permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_RQST_PERMISSION);
            return;
        }
        //没有多个权限，判断单个权限
        String permission = getIntent().getStringExtra(EXTRA_PERMISSION_NAME);
        if (!TextUtils.isEmpty(permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE_RQST_PERMISSION);
            return;
        }
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_RQST_PERMISSION) {
            PermissionManager.onRequestPermissionResult(name,permissions, grantResults);
        }
    }
}
