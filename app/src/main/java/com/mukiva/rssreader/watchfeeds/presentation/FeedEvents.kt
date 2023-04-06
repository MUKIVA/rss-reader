package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.FeedSummary

sealed class FeedEvents {
    data class DeleteRssEvent(val feed: FeedSummary) : FeedEvents()
    data class ShowToastEvent(val msgId: Int) : FeedEvents()
    object AddRssEvent : FeedEvents()
    data class ShowFeedDetailsEvent(val feed: FeedSummary) : FeedEvents()
}