package com.example.wangduwei.demos.schedule.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.wangduwei.demos.AppState;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/9/12
 */

public class JobSchedulerManager {

    private JobScheduler mJobScheduler;
    private long INTERVAL_8_HOUR = 8 * 60 * 1000 * 60;

    private JobSchedulerManager() {
        mJobScheduler = (JobScheduler) AppState.INSTANCE.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    private static class HolderClass {
        private static final JobSchedulerManager instance = new JobSchedulerManager();
    }

    public static JobSchedulerManager getInstance() {
        return HolderClass.instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JobInfo buildJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(AppState.INSTANCE.getContext(),
                JobSchedulerService.class.getName()));

        builder.setPeriodic(INTERVAL_8_HOUR);//8小时一次
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int schedule(JobInfo jobInfo) {
        return mJobScheduler.schedule(jobInfo);
    }


}
