package com.example.customview.floatview;

/**
 * <p>
 *
 * @auther : wangduwei
 * @since : 2019/8/13  16:36
 **/
public interface ICountDown {

    void start();

    void stop();

    void setShowProgress(boolean isShow);

    void setTimeRemained(long remained);

    /**
     * 设置倒计时的时间
     *
     * @param timeMillis 倒计时多久
     */
    void setCountDownTime(long timeMillis);

    /**
     * 倒计时多少分钟
     *
     * @param minutes
     */
    void setCountDownTime(int minutes);

    void setFinishListener(ICountDownFinishListener l);
}
