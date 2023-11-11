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
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay

private const val GET_USER_INFORMATION_DELAY = 1_000L
private const val USER_NAME = "John Doe"
private const val ACCOUNT_NUMBER = "ABCDEFG_12345"
private const val PHONE_NUMBER = "(123) 456-7890"

/**
 * Returns the user's information, including their name, account number and phone number. The
 * information is returned after [GET_USER_INFORMATION_DELAY] milliseconds, simulating the time
 * a network request would take in reality to fetch this information from a server.
 *
 * This class demonstrates exposing [LiveData] generated with a [liveData] builder.
 *
 * Inside a [liveData] builder, An item is emitted through this [LiveData] instance using the
 * [LiveDataScope.emit] method.
 *
 * The [delay] method allows to delay/pause the execution of the coroutine without blocking the
 * thread it's running on.
 */
class GetUserInformationUseCase {

  fun get(): LiveData<UserInformation> = liveData {
    delay(GET_USER_INFORMATION_DELAY)
    emit(UserInformation(USER_NAME, ACCOUNT_NUMBER, PHONE_NUMBER))
  }
}

data class UserInformation(val name: String, val accountNumber: String, val phoneNumber: String)