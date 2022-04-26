package com.example.wangduwei.demos.jni;

import androidx.annotation.Keep;

/**
 * @desc: 生成头文件：
 * 打开studio下面的terminal
 * cd app/src/main/java
 * javah -d ../jni com.example.wangduwei.demos.jni.JniCenter
 * -d指定产生的头文件放在jni目录下
 * @auther:duwei
 * @date:2019/2/27
 */
@Keep
public class JniCenter {

    static {
        System.loadLibrary("jni_demo");
    }

    //==========方法操作==============
    /**
     * 静态方法
     * @return
     */
    public static native String getStringFromC();

    /**
     * 实例方法
     * @return
     */
    public native String getStringFromCObjectMethod();


    public native void changeArray(int[] array);

    //=============全局变量操作======

    private String str = "123";

    public String getStr() {
        return str;
    }

    public static int getsNum() {
        return sNum;
    }

    private static int sNum;

    /**
     * 访问java的成员变量，将值设置为123
     */
    public native void accessJavaField();

    /**
     * 访问java的成员变量，将值设置为456
     */
    public native void accessJavaField2();

    /**
     * 访问静态变量，设置成200
     */
    public native void accessJavaStatisticField();


    //_=============数组操作=======


    /**
     * native 层求和
     * @param array
     * @return
     */
    public native int sumArray(int[] array);

    /**
     * 通过获取数组元素实现
     * @param array
     * @return
     */
    public native int sumArray2(int[] array);


    /**
     * 在底层初始化数据
     * @param size
     * @return
     */
    public static native int[][] initArrayInNative(int size);


    public native void subscribeListener(JNIListener listener);

}
