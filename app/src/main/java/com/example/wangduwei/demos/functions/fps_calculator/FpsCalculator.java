package com.example.wangduwei.demos.functions.fps_calculator;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Choreographer;
import android.widget.TextView;

import com.example.wangduwei.demos.functions.threadpool.AsyncThreadTask;

/**
 * 帧率计算
 *
 * @auther:duwei
 * @date:2019/1/30
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class FpsCalculator implements Choreographer.FrameCallback {
    private long newFrameTimeNanox = 0;
    private long lastFrameNanox = 0;

    private int frameCount = 0;
    private static final long INTERVAL = 1000;

    private boolean start = false;

    private TextView textView;

    public FpsCalculator(TextView view) {
        textView = view;
    }

    //定时任务
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            calculateFPS();
            if (start) {
                AsyncThreadTask.executeDelayed(runnable, INTERVAL);
            }
        }
    };

    private void calculateFPS() {
        Log.d("wdw-fps", "计算帧率");

        float spendTime = (newFrameTimeNanox - lastFrameNanox) / 1000000.0F;//毫秒

        final int result = (int) (frameCount * 1000 / spendTime);//帧每秒

        lastFrameNanox = newFrameTimeNanox;
        frameCount = 0;

        Log.d("wdw-fps-result", "结果 = " + result + "帧/秒");
        AsyncThreadTask.executeDelayedToUI(new Runnable() {
            @Override
            public void run() {
                textView.setText(result + "帧/秒");
            }
        }, 0);

    }

    public void start() {
        start = true;
        AsyncThreadTask.executeDelayed(runnable, INTERVAL);

        Choreographer.getInstance().postFrameCallback(this);
    }


    //新的一帧被渲染的时候回调
    @Override
    public void doFrame(long frameTimeNanos) {
        Log.d("wdw-fps", "新的一帧被渲染 时间= " + frameTimeNanos);
        frameCount++;
        newFrameTimeNanox = frameTimeNanos;
        //下一帧的回调
        if (start) {
            Choreographer.getInstance().postFrameCallback(this);
        }
    }

    public void stop() {
        start = false;
    }


}
