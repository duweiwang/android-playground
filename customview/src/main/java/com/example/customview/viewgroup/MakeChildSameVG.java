package com.example.customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/6/15
 */

public class MakeChildSameVG extends ViewGroup {
    public MakeChildSameVG(Context context) {
        super(context);
    }

    public MakeChildSameVG(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MakeChildSameVG(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int childCount = getChildCount();
        int max = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int width = childView.getMeasuredWidth();
            if (width > max) {
                max = width;
            }
        }
        measureChildren(MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int width = r - l;
        int childTop = 0, childLeft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);

            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();

            if (childLeft + childW > width) {
                childTop += childH;
                childLeft = 0;
            }

            childView.layout(childLeft, childTop, childLeft + childW, childTop + childH);
            childLeft += childW;
        }

    }
}
