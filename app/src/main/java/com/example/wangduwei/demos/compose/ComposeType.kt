package com.example.wangduwei.demos.compose

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * @author 杜伟
 * @date 2022/4/22 5:18 PM
 *
 */
@Keep
@Parcelize
class ComposeType(val name: String) : Parcelable
