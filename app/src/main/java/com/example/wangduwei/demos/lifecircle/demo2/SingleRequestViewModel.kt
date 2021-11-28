package com.example.wangduwei.demos.lifecircle.demo2

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *   <p>
 *  @author : wangduwei
 *  @since : 2020/5/12  13:59
 **/
class SingleRequestViewModel : BaseViewModel<String>() {
    fun performRequest() {
        viewModelScope.launch {
            delay(1000L)
            _mData.value = "返回结果"
        }
    }

}