package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.Feed

sealed class FeedEvents {
    data class DeleteRssEvent(val feed: Feed) : FeedEvents()
    data class ShowToastEvent(val msgId: Int) : FeedEvents()
    object AddRssEvent : FeedEvents()
    data class ShowFeedDetails(val feed: Feed) : FeedEvents()
}