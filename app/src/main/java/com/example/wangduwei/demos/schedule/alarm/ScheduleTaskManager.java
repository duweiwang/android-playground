package com.example.wangduwei.demos.schedule.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.util.SparseArray;

import com.example.wangduwei.demos.AppEnv;
import com.example.wangduwei.demos.AppState;
import com.example.wangduwei.demos.utils.SystemUtil;

import java.util.ArrayList;

public class ScheduleTaskManager extends BroadcastReceiver {

    public static final int TASK_ID_REFRESH_WEATHER_DATA = 0;

    private static ScheduleTaskManager sInstance;
    private final AlarmManager mAlarmManager;
    private final WifiManager mWifiMgr;

    private Context mContext;
    private ScheduleTaskFactory mFactory = new ScheduleTaskFactory();
    private ArrayList<ScheduleTask> mPendingTasks = new ArrayList<>();
    private SparseArray<ScheduleTask> mTaskMap = new SparseArray<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static ScheduleTaskManager getInstance() {
        if (sInstance == null) {
            sInstance = new ScheduleTaskManager();
        }
        return sInstance;
    }

    private ScheduleTaskManager() {
        mContext = AppState.INSTANCE.getContext();
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mWifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mContext.registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra instanceof NetworkInfo) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                int type = networkInfo.getType();
                NetworkInfo.State state = networkInfo.getState();
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    if (state == NetworkInfo.State.CONNECTED) {
                        startPendingTasks();
                    }
                }
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            WifiInfo wifiInfo = mWifiMgr.getConnectionInfo();
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra instanceof NetworkInfo) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (wifiInfo != null && state == NetworkInfo.State.CONNECTED && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        startPendingTasks();
                    }
                }
            }
        } else {
            for (int i = 0; i < mTaskMap.size(); i++) {
                int key = mTaskMap.keyAt(i);
                ScheduleTask task = mTaskMap.get(key);
                if (task.onReceive(action)) {
                    break;
                }
            }
        }
    }

    private void startPendingTasks() {
        if (!SystemUtil.isNetworkOk(mContext)) {
            return;
        }
      new Thread(new Runnable() {
            @Override
            public void run() {
                for (final ScheduleTask task : mPendingTasks) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            task.restart();
                        }
                    });
                    SystemClock.sleep(1000);
                }
                mPendingTasks.clear();
            }
        }).start();
    }

    private void addPendingTask(final ScheduleTask task) {
        for (final ScheduleTask t : mPendingTasks) {
            if (t.mId == task.mId) {
                //已经添加，返回
                return;
            }
        }
        mPendingTasks.add(task);
    }

    private abstract class ScheduleTask {

        private int mId = -1;
        private long mInterval;
        private String mCheckedTimeKey;
        private String mAction;
        private PendingIntent mPendingIntent;
        private Runnable mStartRunnable;

        public ScheduleTask(int id, long interval, String checkedTimeKey, String action) {
            mId = id;
            mInterval = interval;
            mCheckedTimeKey = checkedTimeKey;
            mAction = action;
        }

        public int getId() {
            return mId;
        }

        public long getInterval() {
            return mInterval;
        }

        public String getCheckedTimeKey() {
            return mCheckedTimeKey;
        }

        public String getAction() {
            return mAction;
        }

        public void start(long delay) {
            if (mStartRunnable == null) {
                mStartRunnable = new Runnable() {
                    @Override
                    public void run() {
                        startIntervalTask(false, mInterval, mCheckedTimeKey, mAction);
                    }
                };
            }
//            ThreadExecutorProxy.runOnMainThread(mStartRunnable, delay);
            mHandler.postDelayed(mStartRunnable,delay);
        }

        public void action() {
            if (mCheckedTimeKey == null || mAction == null) {
                throw new IllegalArgumentException();
            }
            if (needNetwork() && !SystemUtil.isNetworkOk(mContext)) {
                addPendingTask(this);
            } else {
                if (doAction()) {
                    startIntervalTask(true, mInterval, mCheckedTimeKey, mAction);
                }
            }
        }

        public void restart() {
            if (mCheckedTimeKey == null || mAction == null) {
                throw new IllegalArgumentException();
            }
            if (doAction()) {
                startIntervalTask(true, mInterval, mCheckedTimeKey, mAction);
            }
        }

        public void cancel() {
            if (mStartRunnable != null) {
//                ThreadExecutorProxy.cancel(mStartRunnable);
                mHandler.removeCallbacks(mStartRunnable);
            }
            if (mPendingIntent != null) {
                mAlarmManager.cancel(mPendingIntent);
            }
        }


        public boolean onReceive(String action) {
            if (mAction.equals(action)) {
                action();
                return true;
            }
            return false;
        }


        /**
         * 间隔指定时间执行任务
         *
         * @param fromReceiver   是否从Receiver回调
         * @param interval       任务间隔
         * @param checkedTimeKey
         * @param action
         */
        public void startIntervalTask(boolean fromReceiver, long interval, String checkedTimeKey,
                                      String action) {
            try {
                long now = System.currentTimeMillis();
                if (fromReceiver) {
                    setLastCheckedTime(checkedTimeKey, now);
                }
                long toNextIntervalTime = 0; // 下一次上传间隔时间
                long lastCheckUpdate = getLastCheckedTime(checkedTimeKey); // 上一次的检查时间
                if (lastCheckUpdate == 0L) {
                    toNextIntervalTime = 0;
                } else if (now - lastCheckUpdate >= interval) {
                    toNextIntervalTime = 0;
                } else {
                    // 动态调整下一次的间隔时间
                    toNextIntervalTime = interval - (now - lastCheckUpdate);
                }

                if (toNextIntervalTime == 0) {
                    Intent updateIntent = new Intent(action);
                    mContext.sendBroadcast(updateIntent);
                } else {
                    final long triggerTime = System.currentTimeMillis() + toNextIntervalTime;
                    if (mPendingIntent == null) {
                        Intent updateIntent = new Intent(action);
                        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, updateIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                    mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, mPendingIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setLastCheckedTime(String key, long checkedTime) {
            SharedPreferences pref = mContext.getSharedPreferences(AppEnv.Companion.getSHRAED_NAME(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            if (pref != null) {
                editor.putLong(key, checkedTime);
                editor.commit();
            }
        }

        public long getLastCheckedTime(String key) {
            SharedPreferences pref = mContext.getSharedPreferences(AppEnv.Companion.getSHRAED_NAME(), Context.MODE_PRIVATE);
            long lastCheckedTime = 0L;
            if (pref != null) {
                lastCheckedTime = pref.getLong(key, 0L);
            }
            return lastCheckedTime;
        }

        /**
         * 该任务要做的事情全写在这个方法里
         */
        public abstract boolean doAction();

        /**
         * 该任务是否需要网络
         *
         * @return
         */
        public abstract boolean needNetwork();
    }

    private class ScheduleTaskFactory {

        public ScheduleTask getScheduleTask(int taskId) {
            ScheduleTask task = mTaskMap.get(taskId);
            if (task == null) {
                switch (taskId) {
                    case TASK_ID_REFRESH_WEATHER_DATA:
                        task = createRefreshWeatherDataTask();
                        break;
                }
                if (task != null) {
                    mTaskMap.put(taskId, task);
                }
            }
            return task;
        }

        @NonNull
        private ScheduleTask createRefreshWeatherDataTask() {
            return new ScheduleTask(TASK_ID_REFRESH_WEATHER_DATA, 20 * 60 * 1000,
                    "pref_key_cached_time", "broad_action_when_event_occur") {
                @Override
                public boolean doAction() {
                    // TODO: 2018/9/14  do something
                    return true;
                }

                @Override
                public boolean needNetwork() {
                    return true;
                }
            };
        }

    }

    public void startTasks() {
        mFactory.getScheduleTask(TASK_ID_REFRESH_WEATHER_DATA).start(5 * 60 * 1000); //延迟5分钟
    }

    public void cancelTask(int taskId) {
        ScheduleTask task = mTaskMap.get(taskId);
        if (task != null) {
            task.cancel();
            mTaskMap.remove(taskId);
        }
    }

    public void startTask(int taskId) {
        mFactory.getScheduleTask(taskId).start(0);
    }

}
