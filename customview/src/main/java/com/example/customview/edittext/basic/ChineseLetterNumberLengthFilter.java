package com.example.customview.edittext.basic;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 限制字符（汉字+字符+数字）的长度，两个英文/数字字符算一个汉字
 */
public class ChineseLetterNumberLengthFilter implements InputFilter {

    private int maxLen;
    private Pattern pattern = Pattern.compile("[0-9a-zA-Z]");

    public ChineseLetterNumberLengthFilter(int maxLen) {
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
        Matcher m = pattern.matcher(String.valueOf(source));
        return m.matches();
    }
}
