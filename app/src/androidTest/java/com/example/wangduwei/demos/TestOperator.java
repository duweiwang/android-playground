package com.example.wangduwei.demos;

import android.app.Instrumentation;
import android.graphics.Point;
import android.view.KeyEvent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author : wangduwei
 * @date : 2020/8/23
 * @description :
 */
@RunWith(AndroidJUnit4.class)
class TestOperator {

    private Instrumentation instrumentation;
    private UiDevice uiDevice;

    @Before
    public void setUp() {
        instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);
    }

    @Test
    public void testOperator() throws InterruptedException {
        //滑动操作，500ms
        uiDevice.swipe(1,1,100,100,10);
        //按键
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_HOME);

        //点击
        uiDevice.click(100,100);
        Thread.sleep(100);//点击延时
        //通过资源ID
        uiDevice.findObject(By.res(""))
                .drag(new Point(100,100));
    }

}
