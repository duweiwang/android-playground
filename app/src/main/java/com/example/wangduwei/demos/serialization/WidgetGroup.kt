package com.example.wangduwei.demos.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author 杜伟
 * @date 2022/7/16 9:51 AM
 *
 */
@Serializable
open class WidgetGroup(
    @SerialName("w_n")
    val name: String,
    @SerialName("w_c")
    val category: WidgetCategory,
    @SerialName("w_a")
    val author: WidgetAuthor,
    @SerialName("w_s")
    val size: Size,
    @SerialName("w_p")
    val position: Position,
    @SerialName("w_l")
    val lastModify: Long,
    @SerialName("w_b")
    val build: String = "",
//    val resId: String? = null
) : Widget(type = WidgetConst.ROOT) {

    val children: ArrayList<Widget> = ArrayList()

    fun addWidget(widget: Widget): Boolean {
        if (!children.contains(widget)) {
            return children.add(widget)
        }
        return false
    }

    fun addWidget(widget: Widget, index: Int): Boolean {
        if (!children.contains(widget)) {
            var realIndex = 0
            if (index < 0) {
                realIndex = 0
            } else if (index > children.size) {
                realIndex = children.size
            }
            children.add(realIndex, widget)
            return true
        }
        return false
    }

    fun removeWidget(widget: Widget): Boolean {
        val iterator = children.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next == widget) {
                iterator.remove()
                return true
            }
        }
        return false
    }

    fun findWidgetById(id: String): Widget? {
        var result: Widget? = null
        children.forEach {
            if (it.id == id) {
                result = it
                return result
            }
        }
        return result
    }
}