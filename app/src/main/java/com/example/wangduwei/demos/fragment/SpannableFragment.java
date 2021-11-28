package com.example.wangduwei.demos.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.view.spannable.ClickableImageSpan;
import com.example.wangduwei.demos.view.spannable.ClickableMovementMethod;
import com.example.wangduwei.demos.view.spannable.SpannableStringProcessor;

import java.util.Random;


/**
 * 1.图文混排中，图片的点击
 * 2.TextSwitcher  && ImageSwitcher
 *
 * @auther: davewang
 * @since: 2019/7/19
 **/
@PageInfo(description = "富文本图文混排", navigationId = R.id.fragment_spannable)
public class SpannableFragment extends BaseSupportFragment {
    private TextSwitcher  textSwitcher;
    //字体部分黑色
    private TextView mTextView0;
    private TextView mTextView1;
    private TextView mTextView2;

    private SpannableStringProcessor mProcessor = new SpannableStringProcessor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spannable, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProcessor.onViewCreated(view.findViewById(R.id.spannable_container));

        textSwitcher = view.findViewById(R.id.text_switcher);
        mTextView0 = view.findViewById(R.id.partly_black);
        mTextView0.setText(Html.fromHtml(String.format(getResources().getString(R.string.fa_song_pay_dialog_msg),"这里演示",1,2)));

        view.findViewById(R.id.change_text).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.change_text: {
                textSwitcher.setText(new Random(5).nextInt()+"");
            }
            break;
            case R.id.change_image: {

            }
            break;
        }
    }
}