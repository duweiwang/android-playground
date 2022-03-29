package com.example.wangduwei.project.dialog;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 杜伟
 * @date 2022/3/29 6:22 PM
 */
public class RunList<P, R> implements Runnable {
    private static Executor sExecutor = Executors.newCachedThreadPool();
    private static Handler sMainHandler = new Handler(Looper.getMainLooper());

    private P mParam;
    private R mResult;

    private boolean mRunOnMain;
    private WeakReference<IRun<P, R>> mRunRef;

    private RunList<?, P> mPrev;
    private RunList<R, ?> mNext;

    private RunList(boolean runOnMain, IRun<P, R> iRun) {
        this(runOnMain, iRun, null);
    }

    private RunList(boolean runOnMain, IRun<P, R> iRun, P param) {
        mRunOnMain = runOnMain;
        mRunRef = new WeakReference<>(iRun);
        mParam = param;
    }

    public static <P, R> RunList<P, R> runOnUiThread(IRun<P, R> iRun, P param) {
        return new RunList<>(true, iRun, param);
    }

    public static <P, R> RunList<P, R> runOnBackground(IRun<P, R> iRun, P param) {
        return new RunList<>(false, iRun, param);
    }

    public <NR> RunList<R, NR> runOnUiThread(IRun<R, NR> iRun) {
        return append(true, iRun);
    }

    public <NR> RunList<R, NR> runOnBackground(IRun<R, NR> iRun) {
        return append(false, iRun);
    }

    private <NR> RunList<R, NR> append(boolean runOnMain, IRun<R, NR> iRun) {
        RunList<R, NR> runList = new RunList<>(runOnMain, iRun);
        this.mNext = runList;
        runList.mPrev = this;
        return runList;
    }

    public void start() {
        if (mPrev != null) {
            mPrev.start();
        } else {
            run();
        }
    }

    @Override
    public void run() {
        boolean onMain = Looper.getMainLooper().isCurrentThread();
        if (mRunOnMain && !onMain) {
            sMainHandler.post(this);
            return;
        }
        if (!mRunOnMain && onMain) {
            sExecutor.execute(this);
            return;
        }
        if (mParam == null && mPrev != null) {
            mParam = mPrev.getResult();
        }
        IRun<P, R> iRun = mRunRef.get();
        if (iRun == null) {
            return;
        }
        mResult = iRun.run(mParam);
        if (mNext != null) {
            mNext.run();
        }
    }

    private R getResult() {
        return mResult;
    }

    public interface IRun<PARAM, RESULT> {
        RESULT run(PARAM param);
    }

}
