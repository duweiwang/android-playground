package com.example.wangduwei.demos.permission.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * @desc: {@code  Manifest.permission.ACCESS_COARSE_LOCATION}这个权限是不会弹框的
 * @auther:duwei
 * @date:2018/10/30
 */

public class PermissionFragment extends BaseSupportFragment {

    private Button mButton;
    private static final int REQUEST_CODE_CAMERA = 1000;
    private String[] multiPermission = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW
//            Manifest.permission.ACCESS_COARSE_LOCATION
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.CAMERA,
//           Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.permission,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButton = view.findViewById(R.id.request_permission);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("wdw", "发起请求权限");
                requestPermissions(multiPermission, REQUEST_CODE_CAMERA);

//                PermissionManager.requestPermission(multiPermission[0], new ISingleRequestCallback() {
//                    @Override
//                    public void onPermissionGranted() {
//
//                    }
//
//                    @Override
//                    public void onPermissionDenyed() {
//
//                    }
//                });

               /* PermissionManager.requestPermissions("PermissionName", multiPermission, new IMultiRequestCallback() {
                    @Override
                    public void onCallback(List<String> granted, List<String> denyed) {

                    }
                });*/
            }
        });


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ActivityCompat.requestPermissions(PermissionFragment.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_CODE_CAMERA);
//            }
//        },5000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (permissions[0].equals(Manifest.permission.CAMERA)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("wdw", "权限通过");
                } else {
                    Log.d("wdw", "权限拒绝");

                    /**
                     * 这个值，没勾选《不再提醒》前，全部返回true
                     * 勾选了《不再提醒》后，全部返回false
                     */
                    boolean shouldShow =shouldShowRequestPermissionRationale(permissions[0]);

                    Log.d("wdw", "shouldShowRequestPermissionRationale = " + shouldShow);
                }
            }
        }
    }
}
