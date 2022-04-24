package com.example.lib_processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/1/6  15:39
 **/
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface PageInfo {
    /**
     * 页面功能描述
     */
    String description();

    /**
     * 页面fragment对应的ID
     */
    int navigationId();

    /**
     *
     * @return
     */
    String title() default "";

    /**
     *
     * @return
     */
    int preview() default  -1;
}
