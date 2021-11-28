package com.example.customview.pop;

import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;

import com.example.customview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * 弹出气泡，类似popupWindow(未完待续...)
 * </p>
 *
 * @auther: davewang
 * @since: 2019/7/16
 **/
public class PopView {

    @Position
    private int mPosition = Position.TOP;
    private View mTargetView;
    private int mWidth, mHeight;
    private String mTipText;

    @IntDef({Position.LEFT, Position.TOP, Position.RIGHT, Position.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
        int LEFT = 1;
        int TOP = 2;
        int RIGHT = 3;
        int BOTTOM = 4;
    }

    public PopView(PopView.Builder builder) {
        this.mPosition = builder.mPosition;
        this.mTargetView = builder.mTargetView;
        this.mWidth = builder.mWidthDp;
        this.mHeight = builder.mHeightDp;
        this.mTipText = builder.mTipText;
    }

    public void show() {
        if (mTargetView == null) {
            throw new IllegalStateException("call Builder#setTargetView first");
        }
        View view = LayoutInflater.from(mTargetView.getContext()).inflate(R.layout.pop_view_layout, null);
        TextView textView = view.findViewById(R.id.pop_view_text);
        ImageView imageView = view.findViewById(R.id.pop_view_arrow);

        RelativeLayout.LayoutParams lpText = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        if (mWidth != 0) {
            lpText.width = dp2px(mWidth);
        }
        if (mHeight != 0) {
            lpText.height = dp2px(mHeight);
        }
        textView.setLayoutParams(lpText);

        textView.setText(mTipText);

        int[] viewLocation = new int[2];
        mTargetView.getLocationInWindow(viewLocation);

        layoutArrow(imageView, textView);

        final PopupWindow popupWindow = new PopupWindow(view, mWidth, mHeight);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);

        switch (mPosition) {
            case Position.LEFT:
                popupWindow.showAsDropDown(mTargetView, 15, mHeight / 2);
                break;
            case Position.BOTTOM:
                popupWindow.showAsDropDown(mTargetView, -mTargetView.getMeasuredWidth() / 2, 15);
                break;
            case Position.RIGHT:
                popupWindow.showAsDropDown(mTargetView, -mTargetView.getMeasuredWidth() - mWidth,
                        -mHeight / 2);
                break;
            case Position.TOP:
                popupWindow.showAsDropDown(mTargetView, -mTargetView.getMeasuredWidth() / 2,
                        -mTargetView.getMeasuredHeight() - mHeight);
                break;
        }


    }

    public static class Builder {
        private int mPosition;
        private View mTargetView;
        private int mWidthDp, mHeightDp;
        private String mTipText;

        public Builder setPosition(@Position int position) {
            mPosition = position;
            return this;
        }

        public Builder setTargetView(View view) {
            mTargetView = view;
            return this;
        }

        public Builder setWidth(int dp) {
            mWidthDp = dp;
            return this;
        }

        public Builder setTipText(String tipText) {
            mTipText = tipText;
            return this;
        }

        // TODO: 2019/7/16 setRadius(){}
        // TODO: 2019/7/16 setBackgroundColor(){}

        public Builder setHeight(int dp) {
            mHeightDp = dp;
            return this;
        }

        public PopView build() {
            return new PopView(this);
        }
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mTargetView.getContext().getResources().getDisplayMetrics());
    }

    private void layoutArrow(ImageView imageView, TextView textView) {
        RelativeLayout.LayoutParams lpArrow = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        RelativeLayout.LayoutParams lpText = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        switch (mPosition) {
            case Position.LEFT:
                imageView.setRotation(-90);
                lpArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpText.addRule(RelativeLayout.LEFT_OF, R.id.pop_view_arrow);
                lpArrow.addRule(RelativeLayout.CENTER_VERTICAL);
                lpText.rightMargin = 0;
                lpArrow.leftMargin = -13;
                break;
            case Position.RIGHT:
                imageView.setRotation(90);
                lpArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lpText.addRule(RelativeLayout.RIGHT_OF, R.id.pop_view_arrow);
                lpArrow.addRule(RelativeLayout.CENTER_VERTICAL);
                lpText.leftMargin = 0;
                lpArrow.rightMargin = -13;
                break;
            case Position.TOP:
                lpArrow.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lpText.addRule(RelativeLayout.ABOVE, R.id.pop_view_arrow);
                lpArrow.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
            case Position.BOTTOM:
                imageView.setRotation(180);
                lpText.addRule(RelativeLayout.BELOW, R.id.pop_view_arrow);
                lpArrow.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
        }
        imageView.setLayoutParams(lpArrow);
        textView.setLayoutParams(lpText);
    }

}
