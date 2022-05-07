/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wangduwei.demos.lifecycle.paging.reddit.repository.inMemory.byPage

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wangduwei.demos.lifecycle.paging.reddit.api.RedditApi
import com.example.wangduwei.demos.lifecycle.paging.pagingwithnetwork.reddit.repository.RedditPostRepository

class InMemoryByPageKeyRepository(
    private val redditApi: RedditApi
) : RedditPostRepository {

    override fun postsOfSubreddit(subReddit: String, pageSize: Int) = Pager(
        PagingConfig(pageSize)
    ) {
        PageKeyedSubredditPagingSource(
            redditApi = redditApi,
            subredditName = subReddit
        )
    }.flow
}
