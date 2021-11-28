package com.example.customview.pop.popup;

import android.app.Activity;
import android.view.View;
import android.widget.PopupWindow;

public class EasyPopup extends BasePopup<EasyPopup> {

    private OnViewListener mOnViewListener;

    public static EasyPopup create() {
        return new EasyPopup();
    }

    public static EasyPopup create(Activity context) {
        return new EasyPopup(context);
    }

    public EasyPopup() {

    }

    public EasyPopup(Activity context) {
        setContext(context);
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initViews(View view) {
        if (mOnViewListener != null) {
            mOnViewListener.initViews(view);
        }
    }

    public EasyPopup setOnViewListener(OnViewListener listener) {
        this.mOnViewListener = listener;
        return this;
    }

    public void setTranslationY(int y) {
        PopupWindow popupWindow = getPopupWindow();
        if (popupWindow != null) {
            popupWindow.update(getOffsetX(), getOffsetY() + y, getWidth() ,getHeight());
        }
    }

    public interface OnViewListener {

        void initViews(View view);
    }
}
