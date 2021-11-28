package com.example.customview.edittext;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;


import com.example.customview.R;
import com.example.customview.edittext.basic.ChineseLetterLengthFilter;
import com.example.customview.edittext.basic.EditTextLayout;
import com.example.customview.edittext.basic.EnterInputFilter;

import java.lang.reflect.Field;

public class TeamDeclarationEditLayout extends EditTextLayout {
    public TeamDeclarationEditLayout(Context context) {
        super(context);
        initEdit();
    }

    public TeamDeclarationEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEdit();
    }

    public TeamDeclarationEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEdit();
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
        //限制输入回车键
        getEditTextContent().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    FxToast.showAToast(getContext(), getContext().getText(R.string.fx_sv_unsupported_enter), FxToast.LENGTH_SHORT,FxToast.GRAVITY.CENTER);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setFilters(EditText editTextContent, int maxLength) {
        // 添加过滤规则，1中文+字母字符限制，2长度限制
        editTextContent.setFilters(new InputFilter[]{new EnterInputFilter(), new ChineseLetterLengthFilter(maxLength)});
    }
}
