package com.example.customview.edittext.basic;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceFilterDest = source.toString();
        if (source != null) {
            Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
            Matcher matcher = pattern.matcher(source);
            sourceFilterDest = matcher.replaceAll("");
        }
        return sourceFilterDest;
    }
}
