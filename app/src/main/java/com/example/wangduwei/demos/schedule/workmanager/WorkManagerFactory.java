//package com.example.wangduwei.demos.schedule.workmanager;
//
//import java.util.concurrent.TimeUnit;
//
//import androidx.work.Constraints;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkRequest;
//
///**
// * @desc:
// * @auther:duwei
// * @date:2018/11/9
// */
//
//public class WorkManagerFactory {
//
//
//    public static WorkRequest getOneTimeWork() {
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(CompressWork.class).build();
//        return oneTimeWorkRequest;
//    }
//
//    /**
//     * 带约束条件的任务
//     */
//    public static WorkRequest getOneTimeWorkWithConstraints() {
//        OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder(CompressWork.class);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            Constraints myConstraints = new Constraints.Builder()
//                    .setRequiresDeviceIdle(true)
//                    .setRequiresCharging(true)
//                    // Many other constraints are available, see the
//                    // Constraints.Builder reference
//                    .build();
//            builder.setConstraints(myConstraints);
//        }
//        return builder.build();
//    }
//
//    public static WorkRequest getPeriodicWork() {
//        PeriodicWorkRequest.Builder photoWorkBuilder = new PeriodicWorkRequest.Builder(CompressWork.class,
//                15,//不能小于15分钟：15 * 60 * 1000L
//                TimeUnit.MINUTES);
//        PeriodicWorkRequest photoWork = photoWorkBuilder.build();
//        return photoWork;
//    }
//}