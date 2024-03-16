package com.mukiva.feature.feed.presentation

sealed interface IFeedEvent {

    data object UnknownError : IFeedEvent
    data object InternetConnectionError : IFeedEvent
    data class ScrollToPage(val pageIndex: Int) : IFeedEvent

}