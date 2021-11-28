package com.example.customview.edittext;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


import com.example.customview.R;
import com.example.customview.edittext.basic.ChineseLetterLengthFilter;
import com.example.customview.edittext.basic.EditTextLayout;

import java.lang.reflect.Field;

public class TeamCreateEditLayout extends EditTextLayout {
    public TeamCreateEditLayout(Context context) {
        super(context);
        initEdit();
    }

    public TeamCreateEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEdit();
    }

    public TeamCreateEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEdit();
    }

    @Override
    public void setFilters(EditText editTextContent, int maxLength) {
        // 添加过滤规则，长度限制
        editTextContent.setFilters(new InputFilter[]{new ChineseLetterLengthFilter(maxLength)});
    }

    private void initEdit() {
        if (getEditTextContent() == null) {
            return;
        }
        //改变光标颜色
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(getEditTextContent(), R.drawable.edittext_common_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
