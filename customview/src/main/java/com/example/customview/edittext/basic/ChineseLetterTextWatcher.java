package com.example.customview.edittext.basic;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对字符进行计算，两个英文字符算一个汉字
 */
public class ChineseLetterTextWatcher implements TextWatcher {

    private OnTextLengthChangedListener textLengthChangedListener;
    private Pattern pattern = Pattern.compile("[0-9a-zA-Z]");

    public ChineseLetterTextWatcher(OnTextLengthChangedListener textLengthChangedListener) {
        this.textLengthChangedListener = textLengthChangedListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        float count = 0;
        if (s != null && s.length() > 0) {
            for (int index = 0; index < s.length(); index++) {
                char c = s.charAt(index);
                if (isHalf(c)) {
                    count = count + 0.5f;
                } else {
                    count = count + 1;
                }
            }
        } else {
            count = 0;
        }
        if (textLengthChangedListener != null) {
            textLengthChangedListener.onLengthChanged((int) Math.ceil(count));
        }
    }

    private boolean isHalf(char source) {
        Matcher m = pattern.matcher(String.valueOf(source));
        return m.matches();
    }

    public interface OnTextLengthChangedListener {
        void onLengthChanged(int newLength);
    }
}
