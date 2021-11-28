package com.example.wangduwei.demos.performance.memory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
    private int mFrom;

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
