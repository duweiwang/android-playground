package com.example.customview.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @desc: 布局的展开和合并
 * <p>
 * 属性动画动态改变高度
 * @auther:duwei
 * @date:2018/12/7
 */

public class ExpandableVG extends FrameLayout {

    private float expansion = 1;//实时扩展度，1 = 完全扩展
    public static final int DEFAULT_TIME = 500;//500ms
    ValueAnimator valueAnimator;

    public ExpandableVG(@NonNull Context context) {
        super(context);
    }

    public ExpandableVG(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableVG(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View child = getChildAt(0);
        int height = child.getMeasuredHeight();
        float current = height - Math.round(height * expansion);
        child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec((int) current,MeasureSpec.EXACTLY));
    }

    public void toggle() {
        if (expansion == 1) {
            valueAnimator = ValueAnimator.ofFloat(expansion, 0);
        } else {
            valueAnimator = ValueAnimator.ofFloat(expansion, 1);
        }
        valueAnimator.setDuration(DEFAULT_TIME);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                expansion = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
        valueAnimator.start();
    }

}
