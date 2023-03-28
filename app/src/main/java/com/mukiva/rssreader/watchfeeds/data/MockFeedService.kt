package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.delay
import java.util.*

class MockFeedService : FeedsService {
    private var _feeds = mutableListOf<Feed>()

    init {}

    override suspend fun refreshNews(feedIndex: Int): List<News> {
        val feed = loadFeed(_feeds[feedIndex].newsRepoLink)
        _feeds[feedIndex] = _feeds[feedIndex].copy(
            news = feed.news
        )
        return _feeds[feedIndex].news.toList()
    }

    override suspend fun deleteFeed(feedIndex: Int): List<Feed> {
        _feeds.removeAt(feedIndex)
        return _feeds.toList()
    }

    override suspend fun addFeed(feed: Feed): List<Feed> {
        _feeds.add(feed.copy())
        return _feeds.toList()
    }

    override suspend fun getAllFeeds(): List<Feed> {
        return _feeds.toList()
    }

    override suspend fun getNewsByIndex(feedIndex: Int): List<News> {
        return _feeds[feedIndex].news.toList()
    }

    private suspend fun loadFeed(link: String): Feed {
        delay(2000)
        return Feed(
            title = link,
            description = "Description",
            newsRepoLink = link,
            imageLink = "",
            news = (0..100).map { News(
                title = "Title $it",
                description = "Description $it",
                date = Date(),
                imageLink = ""
            ) }.toList()
        )
    }
}
