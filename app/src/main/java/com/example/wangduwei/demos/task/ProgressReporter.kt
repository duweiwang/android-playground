package com.example.wangduwei.demos.task

/**
 * Created by gabriel on 6/5/18.
 *
 * https://github.com/Criptext/Android-Email-Client
 */
interface ProgressReporter<in T> {

    fun report(progressPercentage: T)

}