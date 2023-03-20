package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mukiva.rssreader.watchfeeds.domain.RssItem

enum class FeedStateType {
    LOADING,
    EMPTY,
    NORMAL
}

data class FeedState(
    val stateType: FeedStateType,
    val feeds: MutableList<RssItem>,
)

class WatchFeedsViewModel : ViewModel() {


    private val _state = MutableLiveData<FeedState>()
    val state: MutableLiveData<FeedState> = _state

    init {
        _state.value = FeedState(
            stateType = FeedStateType.EMPTY,
            feeds = (1..10).map {
                RssItem(
                    rssTitle = "Title $it"
                ) }.toMutableList()
        )
    }

    fun deleteRssFeed(index: Int) {
        throw NotImplementedError()
    }

    fun addRssFeed(item: RssItem) {
        throw NotImplementedError()
    }

    fun showDetailsRssFeed(index: Int) {
        throw NotImplementedError()
    }

    fun refreshRssFeed(index: Int) {
        throw NotImplementedError()
    }
}