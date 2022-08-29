package com.widget.any.view.default

import com.example.wangduwei.demos.serialization.Position
import com.example.wangduwei.demos.serialization.SystemDefaultWidget
import com.example.wangduwei.demos.serialization.WidgetGroup


/**
 * @author 杜伟
 * @date 2022/7/14 2:14 PM
 *
 */
object SystemWidgetFactory {


    fun create(widget: SystemDefaultWidget, position: Position): WidgetGroup {
        return when (widget) {
            SystemDefaultWidget.SYSTEM_DEFAULT_DATE_2_2 -> {
                SystemWidgetDate2x2(position)
            }
            SystemDefaultWidget.SYSTEM_DEFAULT_WEATHER_2_2 -> {
                SystemWidgetWeather2x2(position)
            }
        }
    }


}