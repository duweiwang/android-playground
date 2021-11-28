package com.example.wangduwei.demos.lifecircle.demo2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *   <p>
 *  @author : wangduwei
 *  @since : 2020/5/12  16:15
 **/
open class BaseViewModel<DATA> : ViewModel() {
    protected val _mData: MutableLiveData<DATA> = MutableLiveData()

    fun mData(): LiveData<DATA> = _mData

}