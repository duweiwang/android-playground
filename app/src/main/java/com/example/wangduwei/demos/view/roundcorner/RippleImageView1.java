package com.example.wangduwei.demos.view.roundcorner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.wangduwei.demos.R;

/**
 * @desc: 使用FrameLayout装三层ImageView，底下两层做动画
 * @auther:duwei
 * @date:2018/10/23
 */

public class RippleImageView1 extends FrameLayout {
    private ImageView[] bottomRipple;

    private ImageView mMainImage;

    private int mainImageId;
    private float mainImageViewWidth;
    private float mainImageViewHeight;
    private static final float MAIN_IMAGEVIEW_WIDTH = 80;
    private static final float MAIN_IMAGEVIEW_HEIGHT = 80;

    private static final int MSG_WAVE2_ANIMATION = 1;
    private static final int MSG_WAVE3_ANIMATION = 2;

    AnimationSet[] animationSets;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    bottomRipple[MSG_WAVE2_ANIMATION].startAnimation(animationSets[MSG_WAVE2_ANIMATION]);
                    break;
                case MSG_WAVE3_ANIMATION:
                    bottomRipple[MSG_WAVE2_ANIMATION].startAnimation(animationSets[MSG_WAVE3_ANIMATION]);
                    break;
            }

        }
    };

    public RippleImageView1(@NonNull Context context) {
        this(context, null);
    }

    public RippleImageView1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.RippleImageView1);
        mainImageViewHeight = typedArray.getDimension(
                R.styleable.RippleImageView1_imageViewHeight, MAIN_IMAGEVIEW_HEIGHT);
        mainImageViewWidth = typedArray.getDimension(
                R.styleable.RippleImageView1_imageViewWidth, MAIN_IMAGEVIEW_WIDTH);
        mainImageId = typedArray.getResourceId(R.styleable.RippleImageView1_imageViewResId,
                0);
        typedArray.recycle();

        initView(context);
        initAnimation();
    }

    public void startAnimation() {
        bottomRipple[0].startAnimation(animationSets[0]);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, 500);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, 500 * 2);
    }

    public void stopAnimation() {
        for (int i = 0; i < bottomRipple.length; i++) {
            bottomRipple[i].clearAnimation();
        }
    }

    //*********

    private void initView(Context context) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) mainImageViewWidth,
                (int) mainImageViewHeight);
        lp.gravity = Gravity.CENTER;

        bottomRipple = new ImageView[3];
        for (int i = 0; i < bottomRipple.length; i++) {
            bottomRipple[i] = new ImageView(context);
            bottomRipple[i].setImageResource(R.mipmap.point_empty);
            addView(bottomRipple[i], lp);
        }

        mMainImage = new ImageView(context);
        if (mainImageId != 0) {
            mMainImage.setImageResource(mainImageId);
        }
        addView(mMainImage, lp);
    }

    private void initAnimation() {
        animationSets = new AnimationSet[3];
        for (int i = 0; i < 3; i++) {
            animationSets[i] = initAnimationSet();
        }
    }

    private AnimationSet initAnimationSet() {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setRepeatCount(Animation.INFINITE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.1f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }


}
