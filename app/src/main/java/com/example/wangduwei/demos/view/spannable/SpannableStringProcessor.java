package com.example.wangduwei.demos.view.spannable;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.AbsBusinessDelegate;

/**
 * <p> 在这里处理逻辑
 *
 * @author : wangduwei
 * @since : 2020/4/26  16:52
 **/
public class SpannableStringProcessor extends AbsBusinessDelegate {
    private TextView spannableTextView,mAnimationText;
    private int position = 0;

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);

        ClassLoader classLoader = getClass().getClassLoader();
        Log.d("wdw-loader","classLoader = " + classLoader);
        spannableTextView = (TextView) view.findViewById(R.id.spannable_text);
        spannableTextView.setMovementMethod(ClickableMovementMethod.getInstance());
        mAnimationText = view.findViewById(R.id.animation_text);
        spannableTextView.setText(getResult());
    }

    private SpannableStringBuilder getResult(){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        //前景色
        SpannableString spannableStringForeColor = new SpannableString("我是用来显示前景色");
        ForegroundColorSpan colorSpan01 = new ForegroundColorSpan(Color.parseColor("#0099FF"));
        spannableStringForeColor.setSpan(colorSpan01, 6, spannableStringForeColor.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(spannableStringForeColor).append("\n");

        //文字大小
        SpannableString spannableStringSize = new SpannableString("我是用来显示字体大小的");
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.3f);
        spannableStringSize.setSpan(sizeSpan01, 8, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(0.5f);
        spannableStringSize.setSpan(sizeSpan02, 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableStringSize).append("\n");

        //样式
        SpannableString spannableStringStyle = new SpannableString("我是用来显示字体加粗的");
        StyleSpan styleSpan01 = new StyleSpan(Typeface.BOLD);
        spannableStringStyle.setSpan(styleSpan01, 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableStringStyle).append("\n");

        //删除线
        SpannableString spannableString04 = new SpannableString("我是来显示中划线的");
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString04.setSpan(strikethroughSpan, 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannableString04).append("\n");
        //图文
        SpannableString spannString = new SpannableString("我是可点击的文字");
        CenterVerticalImageSpan clickImageSpan = new CenterVerticalImageSpan(mView.getContext(), R.drawable.icon_god) {
            @Override
            public void onClick(View view) {
                Toast.makeText(mView.getContext(), "点击诸神之神", Toast.LENGTH_LONG).show();
            }
        };
        spannString.setSpan(clickImageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(spannString).append("\n");
        //
        handler.sendEmptyMessage(0x158);

        return spannableStringBuilder;
    }


    @Override
    public void onDestroy() {

    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x158:
                    SpannableString spannableString = new SpannableString("我是有动画的文字");

                    RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.2f);
                    spannableString.setSpan(sizeSpan, position, position + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    mAnimationText.setText(spannableString);
                    position++;
                    if(position >= mAnimationText.getText().toString().length()) {
                        position = 0;
                    }
                    handler.sendEmptyMessageDelayed(0x158, 150);
                    break;
            }
        }
    };
}
