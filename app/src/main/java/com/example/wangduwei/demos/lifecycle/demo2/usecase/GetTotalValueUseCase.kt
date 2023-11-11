/*
 *
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.raywenderlich.stashpile.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay
import kotlin.random.Random

private const val INITIAL_TOTAL_VALUE = 15_000.00
private const val TOTAL_VALUE_DIFF_RANGE = 5.00
private const val TOTAL_VALUE_INITIAL_DELAY_MS = 1_000L
private const val TOTAL_VALUE_UPDATE_RATE_MS = 2_000L

/**
 * Returns the total value of the user's stocks, and updates it every [TOTAL_VALUE_UPDATE_RATE_MS]
 * milliseconds, allowing it to be displayed in real time to the user.
 *
 * This class demonstrates exposing a [LiveData] generated with a [liveData] builder, which
 * internally runs forever until the scope within which the coroutine is running is terminated.
 */
class GetTotalValueUseCase {

  fun get(): LiveData<Double> = liveData {
    emit(0.00)
    delay(TOTAL_VALUE_INITIAL_DELAY_MS)
    emitSource(getTotalValue())
  }

  private fun getTotalValue(): LiveData<Double> = liveData {
    var total = INITIAL_TOTAL_VALUE
    while (true) {
      total = total.moreOrLessWithMargin(TOTAL_VALUE_DIFF_RANGE)
      emit(total)
      delay(TOTAL_VALUE_UPDATE_RATE_MS)
    }
  }

  private fun Double.moreOrLessWithMargin(margin: Double): Double {
    val diff = Random.nextDouble(-margin, margin)
    return this + diff
  }
}