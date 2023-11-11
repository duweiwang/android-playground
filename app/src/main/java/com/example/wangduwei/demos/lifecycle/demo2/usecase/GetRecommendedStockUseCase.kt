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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wangduwei.demos.lifecycle.demo2.ProfileViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val GET_RECOMMENDED_STOCK_DELAY = 1_000L
private val RECOMMENDED_STOCKS = listOf("UBER", "NFLX", "SQ", "DIS", "MSFT", "BABA", "NKE", "LYFT")
private const val REFRESHING = "Refreshing..."

/**
 * Returns a recommended stock for the user to purchase, and allows for it to be refreshed to get
 * the latest recommendation.
 *
 * This class demonstrates the usage of [ViewModel.viewModelScope] and exposes suspend
 * functions that simulate "long running" operations. When [ProfileViewModel] invokes these methods,
 * it uses its scope to launch them so that when it is cleared, the operations are cancelled.
 */
class GetRecommendedStockUseCase(private val ioDispatcher: CoroutineDispatcher) {

  private val _recommendedStock = MutableLiveData<String>()
  val recommendedStock: LiveData<String>
    get() = _recommendedStock

  suspend fun get() = withContext(ioDispatcher) {
    delay(GET_RECOMMENDED_STOCK_DELAY)
    _recommendedStock.postValue(RECOMMENDED_STOCKS.random())
  }

  suspend fun refresh() = withContext(ioDispatcher) {
    _recommendedStock.postValue(REFRESHING)
    get()
  }
}