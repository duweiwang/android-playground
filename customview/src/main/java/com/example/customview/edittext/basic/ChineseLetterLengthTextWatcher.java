package com.example.customview.edittext.basic;

import android.text.Editable;
import android.text.TextWatcher;


/**
 * 对字符进行计算，两个英文字符算一个汉字
 */
public class ChineseLetterLengthTextWatcher implements TextWatcher {

    private OnTextLengthChangedListener textLengthChangedListener;

    public ChineseLetterLengthTextWatcher(OnTextLengthChangedListener textLengthChangedListener) {
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
        return source < 128;
    }

    public interface OnTextLengthChangedListener {
        void onLengthChanged(int newLength);
    }
}
