package com.example.wangduwei.demos.performance.memory;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * <p>被泄露的Activity
 *
 * @auther : wangduwei
 * @since : 2019/9/6  17:25
 **/
public class LeakedTargetActivity extends Activity {

    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int FROM_INNER_CLASS_LEAK = 0;
    public static final int FROM_HANDLER_LEAK = 1;
    //动画导致的泄露
    public static final int FROM_ANIMATOR_LEAK = 2;
    private int mFrom;

    private TextView textView;
    private ValueAnimator warningAnimation = ValueAnimator.ofArgb(Color.RED,
            Color.GREEN, Color.BLUE, Color.YELLOW);

    public static void start(Context context, int from) {
        Intent intent = new Intent(context, LeakedTargetActivity.class);
        intent.putExtra(EXTRA_TYPE, from);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFrom = getIntent().getIntExtra(EXTRA_TYPE, -1);
        if (mFrom == FROM_INNER_CLASS_LEAK) {
            new Thread(new InnerClass()).start();
        }
        if (mFrom == FROM_ANIMATOR_LEAK) {
            FrameLayout fm = new FrameLayout(this);
            textView = new TextView(this);
            textView.setText("11111");
            fm.addView(textView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT));
            setContentView(fm,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

            warningAnimation.setDuration(1000);
            warningAnimation.setRepeatMode(ValueAnimator.REVERSE);
            //定义为无线循环动画
            warningAnimation.setRepeatCount(ValueAnimator.INFINITE);
            warningAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    Log.d("wdw-leak","动画更新");
                    textView.setBackgroundColor((int) animator.getAnimatedValue());
                }
            });
            warningAnimation.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(warningAnimation != null){
//            warningAnimation.end();
//            warningAnimation.cancel();
//        }
    }

    private class InnerClass implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
