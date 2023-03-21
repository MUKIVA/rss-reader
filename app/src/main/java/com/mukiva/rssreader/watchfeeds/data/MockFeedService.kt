package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
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

    override fun refreshNews(feedIndex: Int): MutableList<News> {
        val feed = loadFeed(_feeds[feedIndex].newsRepoLink)
        _feeds[feedIndex].news = feed.news
        return _feeds[feedIndex].news
    }

    override fun deleteFeed(feedIndex: Int) {
        _feeds.removeAt(feedIndex)
    }

    override fun addFeed(link: String): Feed {
        val feed = loadFeed(link)
        _feeds.add(feed)
        return _feeds.last()
    }

    override fun getAllFeeds(): MutableList<Feed> {
        return _feeds
    }

    override fun getNewsByIndex(feedIndex: Int): MutableList<News> {
        return _feeds[feedIndex].news
    }

    private fun loadFeed(link: String): Feed {
        return Feed(
            title = link,
            description = "Description",
            newsRepoLink = link,
            news = (0..1000).map { News(
                title = "Title $it",
                description = "Description $it",
                date = Date()
            ) }.toMutableList()
        )
    }
}
