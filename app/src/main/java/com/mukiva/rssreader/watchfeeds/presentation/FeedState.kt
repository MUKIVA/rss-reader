package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.FeedSummary

data class FeedState(
    val stateType: FeedStateType,
    val feeds: List<FeedSummary>
)
