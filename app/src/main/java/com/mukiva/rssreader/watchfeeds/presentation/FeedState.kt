package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.Feed

data class FeedState(
    val stateType: FeedStateType,
    val feeds: List<Feed>
)
