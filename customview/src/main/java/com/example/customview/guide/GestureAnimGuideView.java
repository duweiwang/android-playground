package com.example.customview.guide;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;


/**
 * 支持在指定目标控件附近显示手势动画引导的控件，目前只支持在目标控件下侧显示手势引导，以后有业务需要再扩展
 */
public class GestureAnimGuideView extends FrameLayout {
    public static final int RES_ID = R.layout.gesture_anim_guide_layout;
    private View mContent;
    private CircleAnimView mAnimGuidePoint;
    private ImageView mIvGesture;
    private TextView mTvips;

    public GestureAnimGuideView(@NonNull Context context) {
        this(context, null);
    }

    public GestureAnimGuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(RES_ID, this);
        mContent = findViewById(R.id.ll_content);
        mAnimGuidePoint = findViewById(R.id.cav_anim_guide_point);
        mIvGesture = findViewById(R.id.iv_gesture);
        mTvips = findViewById(R.id.tv_tips);
        mContent.setVisibility(INVISIBLE);
        mAnimGuidePoint.setVisibility(INVISIBLE);
    }

    public void setTips(String tips) {
        mTvips.setText(tips);
    }

    public void attachTarget(final View targetView) {
        if (targetView == null) {
            return;
        }
        final int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int[] guideLocation = new int[2];
        getLocationOnScreen(guideLocation);
        location[0] -= guideLocation[0];
        location[1] -= guideLocation[1];

        post(new Runnable() {
            @Override
            public void run() {
                mContent.setX(location[0] + targetView.getWidth() / 2 - mAnimGuidePoint.getWidth() / 2);
                mContent.setY(location[1] + targetView.getHeight() + mAnimGuidePoint.getHeight() / 3);
                mAnimGuidePoint.setX(mContent.getX() + mIvGesture.getX() - mAnimGuidePoint.getWidth() / 2);
                mAnimGuidePoint.setY(mContent.getY() + mIvGesture.getY() - mAnimGuidePoint.getHeight() / 2);
                mAnimGuidePoint.startAnim();
                mContent.setVisibility(VISIBLE);
                mAnimGuidePoint.setVisibility(VISIBLE);
            }
        });
    }

    public void destroy() {
        Log.d("hyh", "GestureAnimGuideView: destroy: ");
        mAnimGuidePoint.stopAnim();
    }
}
