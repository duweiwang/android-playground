package com.example.customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 流式布局
 * <p></p>
 * 练习自定义View的onMeasure、onLayout
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int finalWidth = 0, finalHeight = 0;
        int maxWidthInLinea = 0;//当前行总宽度
        int maxHeightInLine = 0;//当前行最高值

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);//todo 这里容易忘

            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            int viewWidthWithMargin = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
            int viewHeightWithMargin = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

            if (maxWidthInLinea + viewWidthWithMargin > widthSize) {
                //加上当前View的宽度后超出了父容器的最大容量，需要换行

                //累计上一行的数据
                finalWidth = Math.max(maxWidthInLinea, finalWidth);
                finalHeight += maxHeightInLine;
                //新一行的首数据
                maxWidthInLinea = viewWidthWithMargin;
                maxHeightInLine = viewHeightWithMargin;

            } else {
                //没超过父容器的最大容量
                //累加宽度
                maxWidthInLinea += viewWidthWithMargin;
                //找最大高度
                maxHeightInLine = Math.max(maxHeightInLine, viewHeightWithMargin);
            }

            if (i == childCount - 1) {
                finalWidth = Math.max(maxWidthInLinea, finalWidth);
                finalHeight += maxHeightInLine;
            }

        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : finalWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : finalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        int top = 0, left = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (childWidth + lineWidth > getMeasuredWidth()) {
                //如果换行,当前控件将跑到下一行，从最左边开始，所以left就是0，而top则需要加上上一行的行高，才是这个控件的top点;
                top += lineHeight;
                left = 0;
                //同样，重新初始化lineHeight和lineWidth
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }
            //计算childView的left,top,right,bottom
            int lc = left + lp.leftMargin;
            int tc = top + lp.topMargin;
            int rc = lc + child.getMeasuredWidth();
            int bc = tc + child.getMeasuredHeight();
            child.layout(lc, tc, rc, bc);
            //将left置为下一子控件的起始点
            left += childWidth;
        }
    }


}
