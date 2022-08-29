package com.example.wangduwei.demos.serialization

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author 杜伟
 * @date 2022/7/16 10:04 AM
 *
 */


class WidgetMain{

    fun obj2Json(): String {

        //      -----------------------
        //      |         |          |
        //      |         |          |
        //      |--------------------|
        //      |         |          |
        //      |         |          |
        //      -----------------------

        val builder = WidgetBuilder.create("widget#1", size = Size(4, 4))
        builder.add(Position(0, 0), SystemDefaultWidget.SYSTEM_DEFAULT_WEATHER_2_2)
        builder.add(Position(0, 1), SystemDefaultWidget.SYSTEM_DEFAULT_DATE_2_2)

        builder.add(Position(1, 0), SystemDefaultWidget.SYSTEM_DEFAULT_WEATHER_2_2)
        builder.add(Position(1, 1), SystemDefaultWidget.SYSTEM_DEFAULT_DATE_2_2)


        val child = builder.selected(Position(1, 1))

        val bg = BackGroundAttr(Bg(BgType.IMAGE, "http://baidu.com/a.jpeg"))

        child?.updateAttrs(bg)

        val groupAttr = GroupedAttr().apply {
            add(TextAttr(Text("111")))
            add(FontAttr(Text("111")))
        }
        child?.updateAttrs(groupAttr)

        return Json.encodeToString(builder.root())
    }

}
