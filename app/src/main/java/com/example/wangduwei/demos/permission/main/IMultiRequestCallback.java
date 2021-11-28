package com.example.wangduwei.demos.permission.main;

import java.util.List;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/11/5
 */

public interface IMultiRequestCallback {

    void onCallback(List<String> granted, List<String> denyed);
}
