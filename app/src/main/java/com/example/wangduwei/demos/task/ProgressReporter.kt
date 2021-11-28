package com.example.wangduwei.demos.task

/**
 * Created by gabriel on 6/5/18.
 */
interface ProgressReporter<in T> {

    fun report(progressPercentage: T)

}