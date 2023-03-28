package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News

interface FeedsService {
    suspend fun refreshNews(feedIndex: Int): List<News>
    suspend fun deleteFeed(feedIndex: Int): List<Feed>
    suspend fun addFeed(feed: Feed): List<Feed>
    suspend fun getAllFeeds(): List<Feed>
    suspend fun getNewsByIndex(feedIndex: Int): List<News>
}