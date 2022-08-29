package com.widget.any.view.default

import com.example.wangduwei.demos.serialization.*

/**
 * @author 杜伟
 * @date 2022/7/14 10:59 AM
 *
 * 这里存放系统默认实现的widget声明
 *
 */


//示例实现：系统默认实现，日期组件，2x2
class SystemWidgetDate2x2(position: Position) : WidgetGroup(
    name = SystemDefaultWidget.SYSTEM_DEFAULT_DATE_2_2.name,
    category = WidgetCategory.SYSTEM,
    author = WidgetAuthor.AUTHOR_SYSTEM,
    size = Size(2, 2),
    position = position,
    lastModify = 0L
)

//示例实现：天气2x2组件
class SystemWidgetWeather2x2(position: Position) : WidgetGroup(
    name = SystemDefaultWidget.SYSTEM_DEFAULT_WEATHER_2_2.name,
    category = WidgetCategory.SYSTEM,
    author = WidgetAuthor.AUTHOR_SYSTEM,
    size = Size(2, 2),
    position = position,
    lastModify = 0L
)