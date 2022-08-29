package com.example.wangduwei.demos.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 背景类型枚举
 */
//@Serializable(with = BgTypeSerializer::class)
enum class BgType(val value: String) {
    IMAGE("img"),
    COLOR("color")
}

@Serializable
class BackGroundAttr(@SerialName("v")val value: Bg) : Attributes(name = AttrConst.BACKGROUND)

@Serializable
class BorderAttr(@SerialName("v")val value: Border) : Attributes(name = AttrConst.BORDER)

@Serializable
class ColorAttr(@SerialName("v")val value: Int) : Attributes(name = AttrConst.COLOR)

@Serializable
class FontAttr(@SerialName("v")val value: Text) : Attributes(name = AttrConst.FONT)

@Serializable
class SizeAttr(@SerialName("v")val value: Size) : Attributes(name = AttrConst.SIZE)

@Serializable
class TextAttr(@SerialName("v")val value: Text) : Attributes(name = AttrConst.TEXT)

@Serializable
class PositionAttr(@SerialName("v")val value: Position) : Attributes(name = AttrConst.POSITION)

@Serializable
class GroupedAttr : Attributes(
    name = "group",
    tp = AttrType.GROUP
) {
    fun add(attributes: Attributes) {
        value.add(attributes)
    }

    val value: MutableList<Attributes>
        get() = mutableListOf()
}
