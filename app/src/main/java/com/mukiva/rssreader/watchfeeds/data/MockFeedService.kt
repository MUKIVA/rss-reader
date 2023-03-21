package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.delay
import java.util.*

class MockFeedService : IFeedsService {
    private var _feeds = mutableListOf<Feed>()

    init {
        _feeds = (0..9).map {
            Feed(
                title = "Title $it",
                description = "Description $it",
                newsRepoLink = "https://google.com",
                news = mutableListOf())
                }.toMutableList()
        }

    override suspend fun refreshNews(feedIndex: Int): MutableList<News> {
        val feed = loadFeed(_feeds[feedIndex].newsRepoLink)
        _feeds[feedIndex].news = feed.news
        return _feeds[feedIndex].news
    }

    override suspend fun deleteFeed(feedIndex: Int) {
        _feeds.removeAt(feedIndex)
    }

    override suspend fun addFeed(link: String): Feed {
        val feed = loadFeed(link)
        _feeds.add(feed)
        return _feeds.last()
    }

    override fun getAllFeeds(): MutableList<Feed> {
        return _feeds
    }

    override suspend fun getNewsByIndex(feedIndex: Int): MutableList<News> {
        return _feeds[feedIndex].news
    }

    private suspend fun loadFeed(link: String): Feed {
        delay(2000)
        return Feed(
            title = link,
            description = "Description",
            newsRepoLink = link,
            news = (0..10000).map { News(
                title = "Title $it",
                description = "Description $it",
                date = Date()
            ) }.toMutableList()
        )
    }
}
