package com.example.wangduwei.demos.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

import java.lang.reflect.Method;

/**
 * @desc:
 * @auther:duwei
 * @date:2018/12/14
 */
/**
 * 内存信息工具类。
 */
public class MemUtil {

    /**
     * 获取内存信息：total、free、buffers、cached，单位MB
     *
     * @return 内存信息
     */
    public static long[] getMemInfo() {
        long memInfo[] = new long[4];
        try {
            Class<?> procClazz = Class.forName("android.os.Process");
            Class<?> paramTypes[] = new Class[] { String.class, String[].class,
                    long[].class };
            Method readProclines = procClazz.getMethod("readProcLines",
                    paramTypes);
            Object args[] = new Object[3];
            final String[] memInfoFields = new String[] { "MemTotal:",
                    "MemFree:", "Buffers:", "Cached:" };
            long[] memInfoSizes = new long[memInfoFields.length];
            memInfoSizes[0] = 30;
            memInfoSizes[1] = -30;
            args[0] = new String("/proc/meminfo");
            args[1] = memInfoFields;
            args[2] = memInfoSizes;
            if (null != readProclines) {
                readProclines.invoke(null, args);
                for (int i = 0; i < memInfoSizes.length; i++) {
                    memInfo[i] = memInfoSizes[i] / 1024;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memInfo;
    }

    /**
     * 获取空闲内存
     *
     * @return 空闲内存
     */
    public static String getFreeMem() {
        long[] memInfo = getMemInfo();
        return Long.toString(memInfo[1] + memInfo[2] + memInfo[3]) + "M";
    }

    /**
     * 获取总内存
     *
     * @return 总内存
     */
    public static String getTotalMem() {
        long[] memInfo = getMemInfo();
        return Long.toString(memInfo[0]) + "M";
    }

    /**
     * 获取空闲内存和总内存拼接字符串
     *
     * @return 总内存
     */
    public static String getFreeAndTotalMem() {
        long[] memInfo = getMemInfo();
        return Long.toString(memInfo[1] + memInfo[2] + memInfo[3]) + "M/"
                + Long.toString(memInfo[0]) + "M";
    }

    /**
     * 获取空闲内存和总内存拼接字符串 该方法引入的目的是一次不重复多次获取内存值，性能优化用
     *
     * @return 总内存
     */
    public static String trans2FreeAndTotalMem(long[] memInfo) {
        return Long.toString(memInfo[1] + memInfo[2] + memInfo[3]) + "M/"
                + Long.toString(memInfo[0]) + "M";
    }

    /**
     * 获取进程内存Private Dirty数据
     *
     * @param context
     * @param pid
     *            进程ID
     * @return nativePrivateDirty、dalvikPrivateDirty、 TotalPrivateDirty
     */
    public static long[] getPrivDirty(Context context, int pid) {

        ActivityManager mAm = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        int[] pids = new int[1];
        pids[0] = pid;
        Debug.MemoryInfo[] memoryInfoArray = mAm.getProcessMemoryInfo(pids);
        Debug.MemoryInfo pidMemoryInfo = memoryInfoArray[0];
        long[] value = new long[3]; // Natvie Dalvik Total
        value[0] = pidMemoryInfo.nativePrivateDirty;
        value[1] = pidMemoryInfo.dalvikPrivateDirty;
        value[2] = pidMemoryInfo.getTotalPrivateDirty();
        return value;
    }

    /**
     * 获取进程内存PSS数据
     *
     * @param context
     * @param pid
     * @return nativePss、dalvikPss、TotalPss
     */
    public static long[] getPSS(Context context, int pid) {
        long[] value = new long[3]; // Natvie Dalvik Total
        if (pid >= 0) {
            int[] pids = new int[1];
            pids[0] = pid;
            ActivityManager mAm = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            Debug.MemoryInfo[] memoryInfoArray = mAm.getProcessMemoryInfo(pids);
            Debug.MemoryInfo pidMemoryInfo = memoryInfoArray[0];

            value[0] = pidMemoryInfo.nativePss;
            value[1] = pidMemoryInfo.dalvikPss;
            value[2] = pidMemoryInfo.getTotalPss();
        } else {
            value[0] = 0;
            value[1] = 0;
            value[2] = 0;
        }

        return value;
    }

    public static long[] getHeapNative() {
        int Native_HeapSize = 0;
        int Native_HeapAlloc = 1;
        long[] value = new long[2];
        value[Native_HeapSize] = Debug.getNativeHeapSize() >> 10;
        value[Native_HeapAlloc] = Debug.getNativeHeapAllocatedSize() >> 10;
        return value;
    }

    public static long[] getHeapDalvik() {
        int Total_HeapSize = 0;
        int Total_HeapAlloc = 1;

        long[] value_total = new long[2];
        value_total[Total_HeapSize] = Runtime.getRuntime().totalMemory() >> 10;
        value_total[Total_HeapAlloc] = (Runtime.getRuntime().totalMemory() - Runtime
                .getRuntime().freeMemory()) >> 10;

        long[] value_native = getHeapNative();

        int Dalvik_HeapSize = 0;
        int Dalvik_HeapAlloc = 1;
        long[] value_dalvik = new long[2];
        value_dalvik[Dalvik_HeapSize] = value_total[Total_HeapSize]
                - value_native[0];
        value_dalvik[Dalvik_HeapAlloc] = value_total[Total_HeapAlloc]
                - value_native[1];

        return value_dalvik;
    }

    /**
     * 获取堆内存数据，精确到KB Get VM Heap Size by calling:
     * Runtime.getRuntime().totalMemory(); Get Allocated VM Memory by calling:
     * Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
     * Get VM Heap Size Limit by calling: Runtime.getRuntime().maxMemory() Get
     * Native Allocated Memory by calling: Debug.getNativeHeapAllocatedSize();
     */
    public static long[] getVM() {
        long[] value = new long[5];
        value[0] = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) >> 10;
        value[1] = Runtime.getRuntime().totalMemory() >> 10;

        value[2] = Debug.getNativeHeapAllocatedSize() >> 10;
        value[3] = Debug.getNativeHeapSize() >> 10;
        value[4] = Debug.getGlobalAllocSize() >> 10;
        return value;
    }
}
