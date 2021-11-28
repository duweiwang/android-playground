package com.example.wangduwei.demos.router;

import android.util.Log;

import com.example.lib_processor.FragmentInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/6  15:47
 **/
public class RouterManager {

    private static RouterManager sInstance = new RouterManager();
    private List<FragmentInfo> mPages = new ArrayList<>();

    public static RouterManager getInstance() {
        return sInstance;
    }

    private RouterManager() {
    }

    public void init() {
        try {
            Class clazz = Class.forName("com.example.wangduwei.demos.router.PageInfoProtocol");
            Object protocol = clazz.newInstance();
            Field list = clazz.getField("list");
            list.setAccessible(true);
            List<FragmentInfo> temp = (List<FragmentInfo>) list.get(protocol);
            if (temp != null && !temp.isEmpty()) {
                mPages.addAll(temp);
            }
        } catch (Exception e) {
            Log.d("wdw-router", "初始化类失败了");
        }
    }

    public List<FragmentInfo> getPages() {
        return mPages;
    }
}
