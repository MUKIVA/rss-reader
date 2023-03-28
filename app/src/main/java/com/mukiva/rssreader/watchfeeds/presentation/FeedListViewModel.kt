package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.FeedsService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FeedListViewModel(
    private val _feedsService: FeedsService,
) : SingleStateViewModel<FeedState>(
    FeedState(
        stateType = FeedStateType.LOADING,
        feeds = mutableListOf()
    )
) {
    private val _maxPageCount = 10
    private val _eventChanel = Channel<FeedEvents>()
    val eventFlow = _eventChanel.receiveAsFlow()

    init {
        loadFeeds()
    }

    suspend fun triggerDeleteFeed(index: Int) {

        val item = getState().feeds[index]
        _eventChanel.send(FeedEvents.DeleteRssEvent(item))
    }

    suspend fun triggerAddFeed() {
        if (getState().feeds.size >= _maxPageCount)
            _eventChanel.send(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            _eventChanel.send(FeedEvents.AddRssEvent)
    }

    suspend fun triggerAboutFeedDialog(index: Int) {
        _eventChanel.send(FeedEvents.ShowFeedDetails(getState().feeds[index]))
    }

    fun deleteFeed(index: Int) = viewModelScope.launch {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _feedsService.deleteFeed(index)
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    }

    fun loadFeeds() = viewModelScope.launch {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _feedsService.getAllFeeds()
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    }

    private fun getFeedStateType(feeds: List<Feed>): FeedStateType {
        return if (feeds.isEmpty()) FeedStateType.EMPTY else FeedStateType.NORMAL
    }
}