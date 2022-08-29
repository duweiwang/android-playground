package com.example.wangduwei.demos.serialization

import kotlinx.serialization.Serializable

/**
 * @author 杜伟
 * @date 2022/7/15 4:48 PM
 *
 */

@Serializable
abstract class AttrValue

@Serializable
data class Border(var type: String, var value: String) : AttrValue()

@Serializable
data class Bg(val type: BgType, val value: String) : AttrValue()

@Serializable
data class Size(var w: Int, var h: Int) : AttrValue()

@Serializable
data class Text(val text: String) : AttrValue()

@Serializable
data class Position(val x: Int, val y: Int) : AttrValue() {
    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (other !is Position)
            return false
        return other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        return (x + y).hashCode()
    }

}