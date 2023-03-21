package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News

interface IFeedsService {
    fun refreshNews(feedIndex: Int): MutableList<News>
    fun deleteFeed(feedIndex: Int)
    fun addFeed(link: String): Feed
    fun getAllFeeds(): MutableList<Feed>
    fun getNewsByIndex(feedIndex: Int): MutableList<News>
}