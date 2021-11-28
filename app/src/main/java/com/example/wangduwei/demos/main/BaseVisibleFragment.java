package com.example.wangduwei.demos.main;

import androidx.fragment.app.Fragment;

/**
 * <p> 可见性问题
 *
 * @author : wangduwei
 * @since : 2020/3/24  11:23
 **/
public class BaseVisibleFragment extends Fragment {

    private boolean isCanShowing = true;

    @Override
    public void onResume() {
        super.onResume();
        isCanShowing = isVisible();
    }

    @Override
    public void onStop() {
        super.onStop();
        isCanShowing = false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isCanShowing = !hidden;
        onVisibleChanged(isVisibleOnScreen());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanShowing = isVisibleToUser;
        onVisibleChanged(isVisibleOnScreen());
    }

    protected void onVisibleChanged(boolean isVisible) {
    }

    public boolean isVisibleOnScreen() {
        if (isCanShowing && getUserVisibleHint() && isVisible()) {
            if (getParentFragment() == null) {
                return true;
            }

            if (getParentFragment() instanceof BaseVisibleFragment) {
                return ((BaseVisibleFragment) getParentFragment()).isVisibleOnScreen();
            } else {
                return getParentFragment().isVisible();
            }
        }
        return false;
    }
}
