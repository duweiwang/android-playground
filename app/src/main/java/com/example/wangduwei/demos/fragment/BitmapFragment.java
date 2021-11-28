package com.example.wangduwei.demos.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

/**
 * 同一张图片在不同分辨率的文件夹下被加载后的尺寸
 *
 * @auther:duwei
 * @date:2017/12/15
 */
@PageInfo(description = "Bitmap加载大小", navigationId = R.id.dragment_bitmap_demo)
public class BitmapFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_bitmap, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ll = view.findViewById(R.id.ll);
        ll.setBackgroundResource(R.mipmap.default_background_cloudy_night_bg);

        int width = ll.getBackground().getIntrinsicWidth();
        int height = ll.getBackground().getIntrinsicHeight();

        StringBuilder sb = new StringBuilder("原图大小：720*1280\n");
        sb.append("手机分辨率：1080*1920->\n");
        sb.append("放入hdpi：1440*2560->手机分辨率高,拉伸图片2倍\n");
        sb.append("放入mdpi：2160*3840->手机分辨率高,拉伸图片3倍\n");
        sb.append("放入xhdpi：1080*1920->手机分辨率高,拉伸图片1.5倍\n");
        sb.append("放入xxhdpi：720*1280->手机和文件夹匹配，显示原大小\n");
        sb.append("放入xxxhdpi：540*960->手机分辨率低于图片，缩小图片？\n");


        TextView tv = view.findViewById(R.id.bitmap_text);
        tv.setTextColor(Color.WHITE);
        tv.setText(sb.toString());
        Log.d("wdw", "width = " + width * 3 + ",height = " + height * 3);
        //hdpi  4320   *7680//-->1440*2560
        //mdpi 6480  *11520//-->2160*3840
        //xhdpi 3240 * 5760//-->1080*1920
        //xxhdpi 2160 *3840//-->720*1280---->加载原始大小
        //xxxhdpi 1620 * 2880//-->540*960
    }

}
