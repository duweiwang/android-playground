package com.example.wangduwei.demos.lifecircle.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * @author 杜伟
 * @date 2021/1/4 下午9:02
 *
 */
class NewsRepository(
        private val newsRemoteDataSource: NewsRemoteDataSource,
        private val userData: UserData
) {
    /**
     * Returns the favorite latest news applying transformations on the flow.
     * These operations are lazy and don't trigger the flow. They just transform
     * the current value emitted by the flow at that point in time.
     */
    val favoriteLatestNews: Flow<List<ArticleHeadline>> = newsRemoteDataSource.latestNews
            // Intermediate operation to filter the list of favorite topics
            .map { news ->
                news.filter {
                    userData.isFavoriteTopic(it)
                }
            }
            // Intermediate operation to save the latest news in the cache
            .onEach { news -> //saveInCache(news)

            }
}