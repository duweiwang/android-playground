package com.example.customview.edittext.basic;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.customview.R;


/**
 * 输入框通用，过滤规则，1中文+字母字符限制，2长度限制
 */
public abstract class EditTextLayout extends RelativeLayout implements ChineseLetterLengthTextWatcher.OnTextLengthChangedListener {

    private EditText editTextContent;
    private TextView editTextLength;
    private TextView editTextLengthMax;

    private int maxLength = 20;
    private String hintText;

    public EditTextLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public EditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(@NonNull Context context, AttributeSet attrs) {
        inflate(context, R.layout.comment_edit_layout, this);
        editTextContent = findViewById(R.id.fx_content_edit);
        editTextLength = findViewById(R.id.fx_count_text);
        editTextLengthMax = findViewById(R.id.fx_count_max);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextLayout);
            maxLength = a.getInt(R.styleable.EditTextLayout_maxLength, 20);
            int resourceId = a.getResourceId(R.styleable.EditTextLayout_hint, 0);
            hintText = resourceId > 0 ? a.getResources().getText(resourceId).toString() : a.getString(R.styleable.EditTextLayout_hint);
            a.recycle();
        }
        editTextContent.setHint(hintText);
        setFilters(editTextContent, maxLength);
        editTextContent.addTextChangedListener(new ChineseLetterLengthTextWatcher(this));
        editTextLengthMax.setText("/" + maxLength);
    }

    public abstract void setFilters(EditText editTextContent, int maxLength);

    @Override
    public void onLengthChanged(int newLength) {
        editTextLength.setText(String.valueOf(newLength));
        if (newLength > 0) {
            editTextLength.setTextColor(getResources().getColor(R.color.fa_c_00D2BB));
        } else {
            editTextLength.setTextColor(getResources().getColor(R.color.fa_c_999999));
        }
    }

    public String getEditText(){
        return editTextContent.getText().toString();
    }

    public void setEditTextBg(int resid){
        editTextContent.setBackgroundResource(resid);
    }

    public EditText getEditTextContent() {
        return editTextContent;
    }
}
