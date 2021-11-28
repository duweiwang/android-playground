package com.example.wangduwei.demos.lifecircle.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author 杜伟
 * @date 2021/1/4 下午9:02
 *
 */
class LatestNewsViewModel(
        private val newsRepository: NewsRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            newsRepository.favoriteLatestNews
                    // Intermediate catch operator. If an exception is thrown,
                    // catch and update the UI
                    .catch { exception -> println(exception) }
                    .collect { favoriteNews ->
                        // Update View with the latest favorite news
                    }
        }
    }
}