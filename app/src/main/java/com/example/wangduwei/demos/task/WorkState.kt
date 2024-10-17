package com.example.wangduwei.demos.task

/**
 * Created by gabriel on 2/19/18.
 *
 * https://github.com/Criptext/Android-Email-Client
 */
sealed class WorkState<out V : Any> {

    class Working<out V : Any> : WorkState<V>()

    data class Done<out V : Any>(val result: V) : WorkState<V>()
}
