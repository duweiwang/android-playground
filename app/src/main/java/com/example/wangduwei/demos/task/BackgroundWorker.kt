package com.example.wangduwei.demos.task

/**
 * Every one of your blocking operations should run inside a BackgroundWorker instance.
 * Created by gabriel on 9/20/17.
 *
 * https://github.com/Criptext/Android-Email-Client
 */
interface BackgroundWorker<T> {
    val canBeParallelized: Boolean

    /**
     * The function to execute on the UI thread with the generated result once the work is done.
     */
    val publishFn: (T) -> Unit

    /**
     * Maps an exception to a result object. This method will be called when the work() function
     * throws an exception, to try to create a result instead of crashing the application.
     */
    fun catchException(ex: Exception): T

    /**
     * This function is always called in background so that it can freely compose network and
     * database operations without lagging the UI. Returns a result that can be published in the
     * UI thread. It should return null if, and only if, cancel()
     * was called in a different thread before every long running operation finishes.
     * @param reporter a ProgressReporter object that the work function can use to report progress
     * to the UI thread.
     */
    fun work(reporter: ProgressReporter<T>): T?

    /**
     * Cancels the work. if it's called before the work function returns, no result is delivered.
     */
    fun cancel()
}

