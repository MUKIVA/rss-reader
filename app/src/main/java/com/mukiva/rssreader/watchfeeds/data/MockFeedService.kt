package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.delay
import java.util.*

class MockFeedService : FeedsService {
    private var _feeds = mutableListOf<Feed>()

    override suspend fun refreshNews(feedIndex: Int): MutableList<News> {
        val feed = loadFeed(_feeds[feedIndex].newsRepoLink)
        _feeds[feedIndex] = _feeds[feedIndex].copy(
            news = feed.news
        )
        return _feeds[feedIndex].news.toMutableList()
    }

    override suspend fun deleteFeed(feedIndex: Int): MutableList<Feed> {
        _feeds.removeAt(feedIndex)
        return _feeds.toMutableList()
    }

    override suspend fun addFeed(feed: Feed): MutableList<Feed> {
        _feeds.add(feed.copy())
        return _feeds.toMutableList()
    }

    override suspend fun getAllFeeds(): MutableList<Feed> {
        return _feeds.toMutableList()
    }

    override suspend fun getNewsByIndex(feedIndex: Int): MutableList<News> {
        return _feeds[feedIndex].news.toMutableList()
    }

    private suspend fun loadFeed(link: String): Feed {
        delay(2000)
        return Feed(
            title = link,
            description = "Description",
            newsRepoLink = link,
            news = (0..100).map { News(
                title = "Title $it",
                description = "Description $it",
                date = Date()
            ) }.toMutableList()
        )
    }
}
