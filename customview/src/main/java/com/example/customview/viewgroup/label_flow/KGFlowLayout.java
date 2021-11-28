package com.example.customview.viewgroup.label_flow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>流式布局，一行填不满会找合适尺寸填补（不能保证顺序）
 *
 * @auther : wangduwei
 * @since : 2019/9/3  13:49
 **/
public class KGFlowLayout extends ViewGroup {

    private int maxLine = 5;
    private static final String TAG = "FlowLayout2";
    private int maxShowItemIndex = -1;

    public KGFlowLayout(Context context) {
        this(context, null);
    }

    public KGFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KGFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected LayoutParams generateLayoutParams(
            LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    /**
     * 设置流式布局最大行数， 默认2行。
     *
     * @param maxLine 最大行数
     */
    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxShowItemIndex = -1;
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        /**
         * 记录行数
         */
        int line = 0;

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {
                line++;
                if (line == maxLine) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                    maxShowItemIndex = i;
                    break;
                }
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth
                : width + getPaddingLeft() + getPaddingRight(), (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight
                : height + getPaddingTop() + getPaddingBottom());
    }

    public int getMaxShowItemIndex() {
        if (maxShowItemIndex == -1) {
            return getChildCount();
        } else {
            return maxShowItemIndex;
        }
    }

    /**
     * 存储所有的View，按行记录
     */
    public List<List<ViewInfo>> mAllViewsForLayout = new ArrayList<>();
    /**
     * 记录每一行的最大高度
     */
    public List<Integer> mLineHeight = new ArrayList<Integer>();

    public List<ViewInfo> mAllViewInfo = new ArrayList<>();

    public final class ViewInfo {

        public View view;
        public int width;
        public int height;

        public boolean isAdded = false;
    }

    @Override
    public void addView(View child) {
        super.addView(child);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViewsForLayout.clear();
        mLineHeight.clear();
        mAllViewInfo.clear();

        int width = getWidth();
        int lineHeight = 0;
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {

            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth == 0)
                continue;

            ViewInfo viewInfo = new ViewInfo();
            viewInfo.view = child;
            viewInfo.width = childWidth + lp.leftMargin + lp.rightMargin;
            viewInfo.height = childHeight + lp.topMargin + lp.bottomMargin;
            viewInfo.isAdded = false;

            mAllViewInfo.add(viewInfo);
        }

        // 存储每一行所有的childView
        List<ViewInfo> lineViews = new ArrayList<>();

        int tmpLineWidth = 0;

        for (int j = 0, size = mAllViewInfo.size(); j < size; j++) {
            ViewInfo viewInfo = mAllViewInfo.get(j);

            if (viewInfo.isAdded)
                continue;

            if (width - tmpLineWidth >= viewInfo.width) {
                lineViews.add(viewInfo);
                viewInfo.isAdded = true;
                tmpLineWidth += viewInfo.width;

                //没有处理递归的情况
                lineHeight = Math.max(lineHeight, viewInfo.height);
                continue;
            } else {
                // search the next
                lookingSuitWidthView(width, lineViews, tmpLineWidth, j, size);
                j--;
            }

            mLineHeight.add(lineHeight);
            mAllViewsForLayout.add(lineViews);

            lineHeight = 0;
            tmpLineWidth = 0;

            lineViews = new ArrayList<>();
        }

        if (lineViews.size() > 0) {
            mLineHeight.add(lineHeight);
            mAllViewsForLayout.add(lineViews);
        }


        int left = 0;
        int top = 0;

        // 得到总行数
        int lineNums = mAllViewsForLayout.size();


        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViewsForLayout.get(i);

            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);

            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                ViewInfo viewInfo = lineViews.get(j);

                if (viewInfo.view.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) viewInfo.view
                        .getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + viewInfo.view.getMeasuredWidth();
                int bc = tc + viewInfo.view.getMeasuredHeight();

                viewInfo.view.layout(lc, tc, rc, bc);

                left += viewInfo.width;
            }
            left = 0;
            top += lineHeight;
        }

    }

    protected void lookingSuitWidthView(int width, List<ViewInfo> lineViews, int tmpLineWidth, int j, int size) {
        int suitableWidth = width - tmpLineWidth;

        for (int k = j + 1; k < size; k++) {
            ViewInfo info = mAllViewInfo.get(k);
            if (suitableWidth > info.width && !info.isAdded) {

                lineViews.add(info);
                info.isAdded = true;
                tmpLineWidth += info.width;
                lookingSuitWidthView(width, lineViews, tmpLineWidth, k, size);
                return;
            }
        }
    }
}


