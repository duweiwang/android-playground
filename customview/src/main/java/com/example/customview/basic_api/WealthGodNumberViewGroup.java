package com.example.customview.basic_api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;


import com.example.customview.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 包裹四个{@link WealthGodNumberView} 的容器对象
 * </p>
 *
 * @auther: davewang
 * @since: 2019/7/4
 **/
public class WealthGodNumberViewGroup extends FrameLayout implements WealthGodNumberView.IFinishListener {
    private List<WealthGodNumberView> mViews = new ArrayList<>();
    private List<HoldViewRunnable> mDelayRunnables = new ArrayList<>();

    private int mStateFlag = 0x00;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IAnimationEndListener mListener;

    private final int RUNNING_FLAG_ONE = 1;
    private final int RUNNING_FLAG_TWO = 1 << 1;
    private final int RUNNING_FLAG_THREE = 1 << 2;
    private final int RUNNING_FLAG_FOUR = 1 << 3;

    public WealthGodNumberViewGroup(Context context) {
        this(context, null);
    }

    public WealthGodNumberViewGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WealthGodNumberViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.blackcard_number_container, this);

        WealthGodNumberView view4 = findViewById(R.id.fa_scroll_number_4);
        view4.setOnFinishListener(this);
        mViews.add(view4);

        WealthGodNumberView view3 = findViewById(R.id.fa_scroll_number_3);
        view3.setOnFinishListener(this);
        mViews.add(view3);

        WealthGodNumberView view2 = findViewById(R.id.fa_scroll_number_2);
        view2.setOnFinishListener(this);
        mViews.add(view2);

        WealthGodNumberView view1 = findViewById(R.id.fa_scroll_number_1);
        view1.setOnFinishListener(this);
        mViews.add(view1);
    }

    public void start() {
        resetState();

        Random random = new Random();
        for (final WealthGodNumberView view : mViews) {
            int delay = random.nextInt(150);
            Log.d("wdw", "延迟 " + delay + "ms开始动画");
            HoldViewRunnable startRunnable = new HoldViewRunnable(view) {
                @Override
                public void action(WealthGodNumberView pView) {
                    pView.start();
                }
            };
            mDelayRunnables.add(startRunnable);
            mHandler.postDelayed(startRunnable, delay);
        }
    }

    public void setValueAndStop(int value) {
        int innerValue = value;
        Random random = new Random();
        for (final WealthGodNumberView view : mViews) {
            final int finishTo = innerValue % 10;
            int delay = random.nextInt(100);
            Log.d("wdw", "延迟结束 delay = " + delay + "ms,停止的值为 = " + finishTo);
            HoldViewRunnable endRunnable = new HoldViewRunnable(view) {
                @Override
                public void action(WealthGodNumberView pView) {
                    pView.setFinalValue(finishTo);
                }
            };
            mDelayRunnables.add(endRunnable);
            mHandler.postDelayed(endRunnable, delay);
            innerValue /= 10;
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (HoldViewRunnable r : mDelayRunnables) {
            mHandler.removeCallbacks(r);
        }
    }


    @Override
    public void onFinish(WealthGodNumberView view) {
        int id = view.getId();
        if (id == R.id.fa_scroll_number_4) {
            mStateFlag |= RUNNING_FLAG_FOUR;
        } else if (id == R.id.fa_scroll_number_3) {
            mStateFlag |= RUNNING_FLAG_THREE;
        } else if (id == R.id.fa_scroll_number_2) {
            mStateFlag |= RUNNING_FLAG_TWO;
        } else if (id == R.id.fa_scroll_number_1) {
            mStateFlag |= RUNNING_FLAG_ONE;
        }
        if (isAllFinished()) {
            if (mListener != null) {
                mListener.onAnimationEnd();
            }
        }
    }

    public void setOnAnimationEndListener(IAnimationEndListener l) {
        mListener = l;
    }

    public interface IAnimationEndListener {
        void onAnimationEnd();
    }

    private boolean isAllFinished() {
        return (mStateFlag & RUNNING_FLAG_FOUR) == RUNNING_FLAG_FOUR &&
                (mStateFlag & RUNNING_FLAG_THREE) == RUNNING_FLAG_THREE &&
                (mStateFlag & RUNNING_FLAG_TWO) == RUNNING_FLAG_TWO &&
                (mStateFlag & RUNNING_FLAG_ONE) == RUNNING_FLAG_ONE;
    }

    private void resetState() {
        mStateFlag = 0x00;
    }

    public static abstract class HoldViewRunnable implements Runnable {
        WeakReference<WealthGodNumberView> mView;

        public HoldViewRunnable(WealthGodNumberView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void run() {
            if (mView != null) {
                final WealthGodNumberView view = mView.get();
                if (view != null) {
                    action(view);
                }
            }
        }

        public abstract void action(WealthGodNumberView view);
    }
}
