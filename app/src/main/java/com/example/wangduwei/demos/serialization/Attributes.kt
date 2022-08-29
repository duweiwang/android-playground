package com.example.wangduwei.demos.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * auth:liuyongyuan
 * time:2022/7/7 10:31
 */
@Serializable
open class Attributes(
    @SerialName("n")
    val name: String,
    @SerialName("a_t")
    val tp: AttrType = AttrType.SINGLE,
    @SerialName("id")
    val id: String = ViewIdGenerator.randomID()
) {

    override fun equals(other: Any?): Boolean {
        if (other !is Attributes) {
            return false
        }
        if (other.name == name && other.id == id) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var res = 17
        res = res * 31 + name.hashCode()
        res = res * 31 + id.hashCode()
        return res
    }
}