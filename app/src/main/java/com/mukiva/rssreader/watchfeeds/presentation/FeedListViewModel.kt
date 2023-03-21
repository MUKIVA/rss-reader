package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.watchfeeds.data.IFeedsService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

enum class FeedStateType {
    LOADING,
    EMPTY,
    NORMAL
}

data class FeedState(
    val stateType: FeedStateType,
    val feeds: MutableList<Feed>,
)

sealed class FeedEvents {
    data class DeleteRssEvent(val feed: Feed) : FeedEvents()
    data class ShowToastEvent(val msgId: Int) : FeedEvents()
    object AddRssEvent : FeedEvents()
    data class ShowFeedDetails(val feed: Feed) : FeedEvents()
}

class FeedListViewModel(
    private val _feedsService: IFeedsService,
) : ViewModel() {
    private val _maxPageCount = 10
    private val _eventChanel = Channel<FeedEvents>()
    private val _state = MutableLiveData<FeedState>()
    val state: MutableLiveData<FeedState> = _state
    val eventFlow = _eventChanel.receiveAsFlow()

    init {
        _state.value = FeedState(
            stateType = FeedStateType.NORMAL,
            feeds = _feedsService.getAllFeeds()
        )
    }

    fun triggerDeleteFeed(index: Int) = viewModelScope.launch {
        val item = _state.value!!.feeds[index]
        _eventChanel.send(FeedEvents.DeleteRssEvent(item))
    }

    fun triggerAddFeed() = viewModelScope.launch {
        if (_state.value!!.feeds.size >= _maxPageCount)
            _eventChanel.send(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            _eventChanel.send(FeedEvents.AddRssEvent)
    }

    fun triggerAboutFeedDialog(index: Int) = viewModelScope.launch {
        _eventChanel.send(FeedEvents.ShowFeedDetails(_state.value!!.feeds[index]))
    }

    fun deleteFeed(index: Int) = viewModelScope.launch {
        _feedsService.deleteFeed(index)
        _state.value = FeedState(
            stateType = FeedStateType.NORMAL,
            feeds = _feedsService.getAllFeeds()
        )
    }
}