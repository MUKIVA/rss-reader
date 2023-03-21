package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News

interface FeedsService {
    suspend fun refreshNews(feedIndex: Int): MutableList<News>
    suspend fun deleteFeed(feedIndex: Int)
    suspend fun addFeed(feed: Feed)
    fun getAllFeeds(): MutableList<Feed>
    suspend fun getNewsByIndex(feedIndex: Int): MutableList<News>
}