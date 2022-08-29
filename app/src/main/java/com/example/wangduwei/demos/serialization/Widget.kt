package com.example.wangduwei.demos.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * auth:liuyongyuan
 * time:2022/7/7 10:27
 */
@Serializable
open class Widget(
    @SerialName("w_t")
    val type: String,
    val id: String = ViewIdGenerator.randomID()
) {

    private val attrs = mutableListOf<Attributes>()//can be list or attr value object

    fun updateAttrs(attr: Attributes) {
        if (!attrs.contains(attr)) {
            attrs.add(attr)
        }
    }

    fun updateAttrs(attrs: List<Attributes>) {
        attrs.forEach {
            updateAttrs(it)
        }
    }

    fun getAllAttrs() = attrs

    fun removeAttrs(attr: Attributes) {
        attrs.remove(attr)
    }

    fun removeAttrs(name: String) {
        attrs.removeAll { it.name == name }
    }

//    @ActionField
//    var action: Action? = null

    // widget的type和tag相同则认为是同一个widget，同一个RootWidget下只允许add唯一一个相同类型相同tag的widget
//    override fun equals(other: Any?): Boolean {
//         if (other !is Widget) {
//             return false
//         }
//        if (other.type != type) {
//            return false
//        }
//        if (other.id != id) {
//            return false
//        }
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var res = 17
//        res = res * 31 + type.hashCode()
//        if (id != null) {
//            res = res * 31 + id.hashCode()
//        }
//        return res
//    }
}