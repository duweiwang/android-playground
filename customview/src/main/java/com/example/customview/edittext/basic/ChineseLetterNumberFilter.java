package com.example.customview.edittext.basic;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 限制输入字符只能是汉字+字母+數字
 */
public class ChineseLetterNumberFilter implements InputFilter {

    private OnTextInvalidateListener onTextInvalidateListener;

    public void setOnTextInvalidateListener(OnTextInvalidateListener onTextInvalidateListener) {
        this.onTextInvalidateListener = onTextInvalidateListener;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 试一下输入 1次 --- source=1次,  start=0, end=2
        // 业务：限制输入字符只能是汉字+字符
        if (TextUtils.isEmpty(source) && start == 0 && end == 0) {
            return null; // keep original
        }
        Pattern p = Pattern.compile("^[0-9a-zA-Z\u4E00-\u9FA5]+$");
        Matcher m = p.matcher(source.toString());
        boolean isMatches = m.matches();
        if (isMatches) {
            return null; // keep original
        } else {
            // input invalidate Char,that need filter
            if (onTextInvalidateListener != null) {
                onTextInvalidateListener.onTextInvalidate(source);
            }
            return "";
        }
    }

    public interface OnTextInvalidateListener {
        void onTextInvalidate(CharSequence source);
    }
}
