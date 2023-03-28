package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.FeedsService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FeedListViewModel(
    private val _feedsService: FeedsService,
) : SingleStateViewModel<FeedState>(
    FeedState(
        stateType = FeedStateType.LOADING,
        feeds = listOf()
    )
) {
    val eventFlow: SharedFlow<FeedEvents> by lazy { _eventFlow }

    private val _maxPageCount = 10
    private val _eventFlow = MutableSharedFlow<FeedEvents>()


    init {
        loadFeeds()
    }

    fun triggerDeleteFeed(index: Int) {
        handleTriggerDeleteFeed(index)
    }

    fun triggerAddFeed() {
        handleTriggerAddFeed()
    }

    fun triggerAboutFeedDialog(index: Int) {
        handleTriggerAboutFeedDialog(index)
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

    private fun handleTriggerDeleteFeed(index: Int) = viewModelScope.launch {
        val item = getState().feeds[index]
        _eventFlow.emit(FeedEvents.DeleteRssEvent(item))
    }

    private fun handleTriggerAddFeed() = viewModelScope.launch {
        if (getState().feeds.size >= _maxPageCount)
            _eventFlow.emit(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            _eventFlow.emit(FeedEvents.AddRssEvent)
    }

    private fun handleTriggerAboutFeedDialog(index: Int) = viewModelScope.launch {
        _eventFlow.emit(FeedEvents.ShowFeedDetails(getState().feeds[index]))
    }
}