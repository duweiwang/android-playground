package com.example.wangduwei.demos.schedule.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * @desc: <p>  需要在Manifest中声明权限
 * <service android:name=".schedule.jobscheduler.JobSchedulerService"
 * android:permission="android.permission.BIND_JOB_SERVICE"/>   </p>
 *
 *
 * @auther:duwei
 * @date:2018/9/12
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {//run on main thread

    /**
     * 如果返回值是false,系统假设这个方法返回时任务已经执行完毕。
     * 如果返回值是true,那么系统假定这个任务正要被执行
     * @param params
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters params) {


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
