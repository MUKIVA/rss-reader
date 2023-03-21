package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.delay

class MockSearchRssService : SearchRssService {
    override suspend fun search(link: String): Feed {
        delay(3000)
        return Feed(
            title = "New feed",
            description = "Some desc",
            newsRepoLink = link,
            news = mutableListOf()
        )
    }
}