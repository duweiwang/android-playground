package com.example.wangduwei.demos.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.utils.DimensUtil;
import com.example.customview.viewgroup.label_flow.KGFlowLayout;
import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.util.Arrays;
import java.util.List;

/**
 * <p> 标签流
 *
 * @author : wangduwei
 * @since : 2019/9/3  13:52
 **/
@PageInfo(description = "标签流", navigationId = R.id.fragment_flow_label)
public class FlowLabelFragment extends BaseSupportFragment {
    private KGFlowLayout mFlowLayout;
    private ImageView mDeleteBtn;

    private TextView mDeleteAll;
    private TextView mFinish;

    private boolean isShowEditHistoryMode;
    private IHistoryClickListener mHistoryClickListener;

    private Button mAddLabel;
    private EditText mInputLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flowlabel, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFlowLayout = view.findViewById(R.id.flow_label);
        mDeleteBtn = view.findViewById(R.id.delete_img);
        mDeleteAll = view.findViewById(R.id.delete_all_text);
        mFinish = view.findViewById(R.id.finish_text);

        mDeleteBtn.setOnClickListener(this);

        mDeleteAll.setOnClickListener(this);
        mFinish.setOnClickListener(this);

        mAddLabel = view.findViewById(R.id.add_label);
        mAddLabel.setOnClickListener(this);
        mInputLabel = view.findViewById(R.id.input_label);

        setHistoryClickListener(new IHistoryClickListener() {
            @Override
            public void onHistoryClick(CharSequence label) {
                Toast.makeText(getActivity(), label, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(CharSequence text) {
                int count = mFlowLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    RelativeLayout relativeLayout = (RelativeLayout) mFlowLayout.getChildAt(i);
                    TextView textView = (TextView) relativeLayout.getChildAt(0);
                    if (TextUtils.equals(textView.getText(), text)) {
                        mFlowLayout.removeViewAt(i);
                    }
                }
            }

            @Override
            public void onClearAllHistory() {
                mFlowLayout.removeAllViews();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.delete_img:
                showEditHistoryMode(true);
                break;
            case R.id.delete_all_text:

                break;
            case R.id.finish_text:
                showEditHistoryMode(false);
                break;
            case R.id.add_label:
                if (TextUtils.isEmpty(mInputLabel.getText())) {
                    Toast.makeText(getActivity(), "请输入标签", Toast.LENGTH_SHORT).show();
                    return;
                }

                genelateHistory(Arrays.asList(mInputLabel.getText().toString()), mFlowLayout);
                mInputLabel.setText("");
                break;
        }
    }


    private void showEditHistoryMode(boolean isShow) {
        if ((isShow && isShowEditHistoryMode) || (!isShowEditHistoryMode && !isShow)) {
            return;
        }
        if (mFlowLayout != null) {
            isShowEditHistoryMode = isShow;
            for (int i = 0; i < mFlowLayout.getChildCount(); i++) {
                RelativeLayout layout = (RelativeLayout) mFlowLayout.getChildAt(i);
                if (layout.getChildCount() >= 2) {
                    final TextView tv = (TextView) layout.getChildAt(0);
                    if (tv != null) {
                        tv.setOnClickListener(isShow ? null : new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mHistoryClickListener.onHistoryClick(tv.getText());
                            }
                        });
                    }
                    View v = layout.getChildAt(1);
                    if (v != null) {
                        v.setVisibility(isShow ? View.VISIBLE : View.GONE);
                    }
                }
            }

            mDeleteBtn.setVisibility(isShow ? View.GONE : View.VISIBLE);
            mFinish.setVisibility(isShow ? View.VISIBLE : View.GONE);
            mDeleteAll.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }


    private String getLimitMaxNumResult(String key) {
        if (TextUtils.isEmpty(key) || key.length() <= 9) {
            return key;
        }
        return key.substring(0, 9) + "…";

    }

    private void setViewBackgroundDrawable(View view, Drawable drawable) {
        int i = view.getPaddingLeft();
        int j = view.getPaddingTop();
        int k = view.getPaddingRight();
        int l = view.getPaddingBottom();
        view.setBackgroundDrawable(drawable);
        view.setPadding(i, j, k, l);
    }

    private Drawable getStrokeRoundCornerStatedDrawable() {
        GradientDrawable defaultDrawable = new GradientDrawable();
        defaultDrawable.setCornerRadius(DimensUtil.dp2px(getActivity(), 14));
        defaultDrawable.setColor(Color.TRANSPARENT);
        defaultDrawable.setColor(Color.parseColor("#000000"));
        defaultDrawable.setAlpha(10);
        return defaultDrawable;
    }

    private void genelateHistory(List<String> searchHistorys, KGFlowLayout flowLayout) {
        int leftRightPadding = DimensUtil.dp2px(getActivity(), 12);
        int textSize = DimensUtil.dp2px(getActivity(), 12);

        int count = searchHistorys.size();

        for (int i = 0; i < count; i++) {
            generateHistoryItem(searchHistorys.get(i), textSize, leftRightPadding, flowLayout);
        }
    }

    private void generateHistoryItem(String historyStr, int textSize, int leftRightPadding, KGFlowLayout flowLayout) {
        String history = getLimitMaxNumResult(historyStr);

        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        //标签文本
        final TextView textView = new TextView(getActivity());
        textView.setTextColor(Color.parseColor("#888888"));
        textView.setText(history);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setClickable(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setIncludeFontPadding(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHistoryClickListener != null) {
                    mHistoryClickListener.onHistoryClick(textView.getText());
                }
            }
        });
        textView.setPadding(leftRightPadding, 0, leftRightPadding, 0);
        setViewBackgroundDrawable(textView, getStrokeRoundCornerStatedDrawable());

        int width = (int) textView.getPaint().measureText(history) + 2 * leftRightPadding;
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(width, DimensUtil.dp2px(getActivity(), 26));
        tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(textView, tvParams);
        //删除按钮
        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(
                DimensUtil.dp2px(getActivity(), 14),
                DimensUtil.dp2px(getActivity(), 14));
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        final ImageButton deleteBtn = new ImageButton(getActivity());
        if (isShowEditHistoryMode) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
        }
        deleteBtn.setContentDescription("删除");
        deleteBtn.setImageResource(R.drawable.search_history_icon_delete);
        deleteBtn.setColorFilter(Color.parseColor("#33101010"), PorterDuff.Mode.SRC_IN);
        deleteBtn.setBackground(null);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHistoryClickListener != null) {
                    mHistoryClickListener.onDeleteClick(textView.getText());
                }
            }
        });
        relativeLayout.addView(deleteBtn, deleteParams);

        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(
                width + DimensUtil.dp2px(getActivity(), 4),
                DimensUtil.dp2px(getActivity(), 31));
        mainParams.rightMargin = DimensUtil.dp2px(getActivity(), 4);
        mainParams.bottomMargin = DimensUtil.dp2px(getActivity(), 7);

        flowLayout.addView(relativeLayout, mainParams);
    }


    public interface IHistoryClickListener {
        void onHistoryClick(CharSequence label);

        void onDeleteClick(CharSequence text);

        void onClearAllHistory();
    }

    public void setHistoryClickListener(IHistoryClickListener l) {
        mHistoryClickListener = l;
    }
}
