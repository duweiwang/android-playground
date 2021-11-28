package com.example.customview.edittext.basic;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 限制字符（汉字+字母）的长度，两个英文字符算一个汉字，中文、英文标点分别是1、0.5
 */
public class ChineseLetterLengthFilter implements InputFilter {

    private int maxLen;

    public ChineseLetterLengthFilter(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        float count = 0;

        while (count <= maxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if (isHalf(c)) {
                count = count + 0.5f;
            } else {
                count = count + 1;
            }
        }

        if (count > maxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= maxLen && sindex < source.length()) {
            char c = source.charAt(sindex++);
            if (isHalf(c)) {
                count = count + 0.5f;
            } else {
                count = count + 1;
            }
        }

        if (count > maxLen) {
            sindex--;
        }

        return source.subSequence(0, sindex);
    }

    private boolean isHalf(char source) {
        return source < 128;
    }
}
