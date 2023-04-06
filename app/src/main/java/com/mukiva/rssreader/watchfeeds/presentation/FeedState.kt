package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.FeedSummary

data class FeedState(
    val stateType: FeedStateType,
    val feeds: List<FeedSummary>,

    val currentPage: Int = 0,
    val scrollMap: Map<Long, Int> = mapOf()
)
