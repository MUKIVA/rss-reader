package com.mukiva.rssreader.watchfeeds.ui

import com.mukiva.rssreader.watchfeeds.domain.News

interface FeedItemEvent {
    fun onItemDetails(item: News)
}