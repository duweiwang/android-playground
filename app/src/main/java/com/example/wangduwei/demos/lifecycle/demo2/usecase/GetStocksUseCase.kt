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

package com.example.wangduwei.demos.lifecycle.demo2.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.delay
import androidx.lifecycle.Transformations

private const val GET_STOCKS_DELAY = 1_000L
private val STOCKS = listOf("GOOG", "AAPL", "AMZN", "TSLA", "TWTR", "FB")

/**
 * Returns the user's stocks as a [String] in the form "stock1, stock2, stock3".
 *
 * This class demonstrates combining exposed [LiveData] with [Transformations] by transforming
 * a [LiveData] instance using the [LiveData.switchMap] method.
 *
 * Note that the transformation method can be a suspend function.
 */
class GetStocksUseCase {

  fun get(): LiveData<String> = getStocks().switchMap { stocks ->
    liveData {
      emit(stocks.joinToString())
    }
  }

  private fun getStocks(): LiveData<List<String>> = liveData {
    delay(GET_STOCKS_DELAY)
    emit(STOCKS)
  }
}