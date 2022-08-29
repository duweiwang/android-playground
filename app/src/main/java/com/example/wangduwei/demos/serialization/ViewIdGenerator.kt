package com.example.wangduwei.demos.serialization

/**
 * auth:liuyongyuan
 * time:2022/7/7 11:05
 */
object ViewIdGenerator {

    fun randomID(): String = List(8) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")

}