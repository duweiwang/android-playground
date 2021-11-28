//package com.example.wangduwei.demos.schedule.workmanager;
//
//import androidx.work.WorkManager;
//import androidx.work.WorkRequest;
//
//import java.util.Arrays;
//
///**
// * @desc:控制器单例
// * @auther:duwei
// * @date:2019/3/27
// */
//public class WorkController {
//    private static WorkController mController;
//
//    private WorkController() {
//    }
//
//    public static WorkController getInstance() {
//        if (mController == null) {
//            mController = new WorkController();
//        }
//        return mController;
//    }
//
//
//    public void doWorks(WorkRequest... request){
//        WorkManager.getInstance().enqueue(Arrays.asList(request));
//    }
//
//    public void doWork(WorkRequest requet){
//        doWorks(requet);
//    }
//
//}
