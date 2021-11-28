package com.example.wangduwei.project.extfun

import kotlinx.coroutines.delay

/**
 * @author 杜伟
 * @date 2/19/21 8:38 PM
 *
 */




/**
 * 失败重试
 */
private suspend fun <T> retry(times: Int,
                              initialDelayMillis: Long = 100,
                              maxDelayMillis: Long = 1000,
                              factor: Double = 2.0,
                              block: suspend () -> T
): T {
    var currentDelay = initialDelayMillis
    repeat(times) {
        try {
            return block()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
    }
    return block() // last attempt
}