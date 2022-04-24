package com.example.wangduwei.demos.gles.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

@PageInfo(description = "使用OpenGL绘制基本的图形",
        navigationId = R.id.fragment_gl_basic,
        title = "OpenGl画图",
        preview = R.drawable.preview_opengl)
public class OpenGLFragment extends BaseSupportFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opengl_shape, null);
    }

    @Override
    public void onResume() {
        super.onResume();
//        mOpenGLView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
//        mOpenGLView.onPause();
    }
}
