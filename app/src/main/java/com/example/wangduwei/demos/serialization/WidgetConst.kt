package com.example.wangduwei.demos.serialization


/**
 * auth:liuyongyuan
 * time:2022/7/7 16:22
 */
object WidgetConst {
    const val ROOT = "root"
    const val IMAGE = "image"
    const val LABEL = "label"

    const val CATEGORY_SYSTEM = "system"
    const val CATEGORY_CUSTOM = "custom"
}

/**
 * widget来源定义
 */
enum class WidgetCategory(value: Int) {
    SYSTEM(1),
    CUSTOM(2)
}

/**
 * 第一期内置的组件列表
 */
enum class SystemDefaultWidget(value: String) {

    SYSTEM_DEFAULT_DATE_2_2("system_default_date_2x2"),
    SYSTEM_DEFAULT_WEATHER_2_2("system_default_weather_2x2")

}

enum class WidgetAuthor(value: String) {
    AUTHOR_SYSTEM("system"),
    AUTHOR_USER("user")
}

//@Serializable(with = AttrTypeSerializer::class)
enum class AttrType(value: String) {
    GROUP("0"),
    SINGLE("1")
}