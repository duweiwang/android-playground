/*
 * Copyright (c) 2018 Razeware LLC
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
 */

package com.duwei.lib_appwidget

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class CoffeeLoggerPersistence(private val context: Context) {

  companion object {
    private const val PREFS_NAME = "com.duwei.lib_appwidget.CoffeeLoggerWidget"
    private const val PREF_PREFIX_KEY = "coffee_logger"
    private const val PREF_LIMIT_PREFIX = "coffee_limit_"
  }

  private val strFormatter = SimpleDateFormat("DDMMYYYY", Locale.US)
  
  // Write the prefix to the SharedPreferences object for this widget
  internal fun saveTitlePref(value: Int) {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putInt(PREF_PREFIX_KEY + strFormatter.format(date), value)
    prefs.apply()
  }

  // Write the prefix to the SharedPreferences object for this widget
  internal fun saveLimitPref(value: Int, widgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putInt(PREF_LIMIT_PREFIX + widgetId, value)
    prefs.apply()
  }

  // Write the prefix to the SharedPreferences object for this widget
  internal fun getLimitPref(widgetId: Int): Int {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getInt(PREF_LIMIT_PREFIX + widgetId, 0)
  }

  // Read the prefix from the SharedPreferences object for this widget.
  // If there is no preference saved, get the default from a resource
  internal fun loadTitlePref(): Int {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getInt(PREF_PREFIX_KEY + strFormatter.format(date), 0)
  }

  internal fun deletePref(widgetId: Int) {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + strFormatter.format(date))
    prefs.remove(PREF_LIMIT_PREFIX + widgetId)
    prefs.apply()
  }
}
