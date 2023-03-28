package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.watchfeeds.domain.News

data class NewsListState(
    val stateType: NewsListStateType,
    val news: List<News>
)