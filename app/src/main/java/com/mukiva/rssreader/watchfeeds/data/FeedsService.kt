package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News

interface FeedsService {
    suspend fun refreshNews(feedIndex: Int): MutableList<News>
    suspend fun deleteFeed(feedIndex: Int): MutableList<Feed>
    suspend fun addFeed(feed: Feed): MutableList<Feed>
    suspend fun getAllFeeds(): MutableList<Feed>
    suspend fun getNewsByIndex(feedIndex: Int): MutableList<News>
}