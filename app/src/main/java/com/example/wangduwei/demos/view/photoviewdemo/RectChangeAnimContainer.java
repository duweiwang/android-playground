package com.example.wangduwei.demos.view.photoviewdemo;

import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.utils.AppUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>将三个{@link PhotoView}抽象成三个{@link RectF}并做动画
 * 由源RectF至目标RectF
 * </p>
 *
 * @auther:duwei
 * @date:2019/3/13
 */
public class RectChangeAnimContainer extends ViewGroup {
    private PhotoView[] mPhotoViews = new PhotoView[3];
    private static final int STATE_START = 0;
    private static final int STATE_END = 1;
    int style = STATE_START;
    private HashMap<Integer, List<RectF>> mStylePosition = new HashMap<>(2);

    //三个矩形的变化动画
    private ValueAnimator objectAnimator1;
    private ValueAnimator objectAnimator2;
    private ValueAnimator objectAnimator3;
    //联合动画
    AnimatorSet animatorSet = new AnimatorSet();

    //保存动画中的值
    private RectF mRectF1, mRectF2, mRectF3;

    public RectChangeAnimContainer(Context context) {
        this(context, null);
    }

    public RectChangeAnimContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (int i = 0; i < 3; i++) {
            mPhotoViews[i] = new PhotoView(context);
            addView(mPhotoViews[i]);
        }

        mPhotoViews[0].setImageResource(R.mipmap.ethnicity_female_other);
        mPhotoViews[1].setImageResource(R.mipmap.ethnicity_female_white);
        mPhotoViews[2].setImageResource(R.mipmap.ethnicity_male_asian);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.makeMeasureSpec(AppUtils.getScreenWidth(), MeasureSpec.EXACTLY);
        int widthSpecHalf = MeasureSpec.makeMeasureSpec(AppUtils.getScreenWidth() / 2, MeasureSpec.EXACTLY);
        int widthSpec3 = MeasureSpec.makeMeasureSpec(AppUtils.getScreenWidth() / 3, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, widthSpec);
        if (style == STATE_START) {
            View view0 = getChildAt(0);
            view0.measure(widthSpec, widthSpecHalf);

            View view1 = getChildAt(1);
            view1.measure(widthSpecHalf, widthSpecHalf);

            View view2 = getChildAt(2);
            view2.measure(widthSpecHalf, widthSpecHalf);
        } else {
            getChildAt(0).measure(widthSpec3, widthSpec);
            getChildAt(1).measure(widthSpec3, widthSpec);
            getChildAt(2).measure(widthSpec3, widthSpec);
        }

        if (!mStylePosition.containsKey(Integer.valueOf(0))) {
            RectF rectF1 = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight() / 2);
            RectF rectF2 = new RectF(0, getMeasuredHeight() / 2, getMeasuredWidth() / 2, getMeasuredHeight());
            RectF rectF3 = new RectF(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight());
            mRectF1 = rectF1;
            mRectF2 = rectF2;
            mRectF3 = rectF3;

            ArrayList<RectF> rects = new ArrayList<>(3);
            rects.add(rectF1);
            rects.add(rectF2);
            rects.add(rectF3);
            mStylePosition.put(Integer.valueOf(0), rects);
        }
        if (!mStylePosition.containsKey(Integer.valueOf(1))) {
            RectF rectF1 = new RectF(0, 0, getMeasuredWidth() / 3, getMeasuredHeight());
            RectF rectF2 = new RectF(getMeasuredWidth() / 3, 0, 2 * getMeasuredWidth() / 3, getMeasuredHeight());
            RectF rectF3 = new RectF(2 * getMeasuredWidth() / 3, 0, getMeasuredWidth(), getMeasuredHeight());
            ArrayList<RectF> rects = new ArrayList<>(3);
            rects.add(rectF1);
            rects.add(rectF2);
            rects.add(rectF3);
            mStylePosition.put(Integer.valueOf(1), rects);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (style == STATE_START) {
            getChildAt(0).layout(
                    (int) mRectF1.left,
                    (int) mRectF1.top,
                    (int) mRectF1.right,
                    (int) mRectF1.bottom);
            getChildAt(1).layout(
                    (int) mRectF2.left,
                    (int) mRectF2.top,
                    (int) mRectF2.right,
                    (int) mRectF2.bottom);
            getChildAt(2).layout(
                    (int) mRectF3.left,
                    (int) mRectF3.top,
                    (int) mRectF3.right,
                    (int) mRectF3.bottom);

        } else {
            getChildAt(0).layout(
                    (int) mRectF1.left,
                    (int) mRectF1.top,
                    (int) mRectF1.right,
                    (int) mRectF1.bottom);
            getChildAt(1).layout(
                    (int) mRectF2.left,
                    (int) mRectF2.top,
                    (int) mRectF2.right,
                    (int) mRectF2.bottom);
            getChildAt(2).layout(
                    (int) mRectF3.left,
                    (int) mRectF3.top,
                    (int) mRectF3.right,
                    (int) mRectF3.bottom);

        }
    }

    public void changeStyle() {
        if (style == 0) {
            objectAnimator1 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(0).get(0),
                    mStylePosition.get(1).get(0));
            objectAnimator2 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(0).get(1),
                    mStylePosition.get(1).get(1));
            objectAnimator3 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(0).get(2),
                    mStylePosition.get(1).get(2));
            style = 1;
        } else {
            objectAnimator1 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(1).get(0),
                    mStylePosition.get(0).get(0));
            objectAnimator2 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(1).get(1),
                    mStylePosition.get(0).get(1));
            objectAnimator3 = ValueAnimator.ofObject(new RectFEvaluator(),
                    mStylePosition.get(1).get(2),
                    mStylePosition.get(0).get(2));
            style = 0;
        }
        animatorSet.playTogether(objectAnimator1, objectAnimator2, objectAnimator3);
        animatorSet.setDuration(1000);
        objectAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRectF1 = (RectF) animation.getAnimatedValue();
                requestLayout();//只要调一个就好了，其他两个改变值就行了
            }
        });
        objectAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRectF2 = (RectF) animation.getAnimatedValue();
//                requestLayout();
            }
        });
        objectAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRectF3 = (RectF) animation.getAnimatedValue();
//                requestLayout();
            }
        });

        animatorSet.start();
    }


    public class RectFEvaluator implements TypeEvaluator<RectF> {
        RectF rectF = new RectF();

        @Override
        public RectF evaluate(float fraction, RectF startValue, RectF endValue) {
            rectF.set(
                    (startValue.left + (endValue.left - startValue.left) * fraction),
                    startValue.top + (endValue.top - startValue.top) * fraction,
                    startValue.right + (endValue.right - startValue.right) * fraction,
                    startValue.bottom + (endValue.bottom - startValue.bottom) * fraction
            );
            return rectF;
        }
    }
}
