package com.example.wangduwei.demos.view.photoviewdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
@PageInfo(description = "对矩形做动画",navigationId = R.id.fragment_animation_photo_view)
public class UsePhotoViewDemo extends BaseSupportFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup c, @Nullable Bundle savedInstanceState) {
        final RectChangeAnimContainer container = new RectChangeAnimContainer(getActivity());
        Button button = new Button(getActivity());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.changeStyle();
            }
        });
        button.setText("Change");
        button.setTextSize(20);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(container);
        linearLayout.addView(button);
        return linearLayout;
    }
}
