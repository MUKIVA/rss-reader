package com.mukiva.rssreader.watchfeeds.presentation

sealed class NewsListEvents {
    data class RefreshErrorEvent(val msgId: Int) : NewsListEvents()
}