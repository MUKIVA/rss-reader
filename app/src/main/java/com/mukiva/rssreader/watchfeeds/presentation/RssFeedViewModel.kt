package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mukiva.rssreader.watchfeeds.domain.FeedItem
import com.mukiva.rssreader.watchfeeds.data.FeedItemsService
import com.mukiva.rssreader.watchfeeds.data.NewsListener

enum class ListStateType {
    LOADING,
    EMPTY,
    FAIL,
    NORMAL
}

data class FeedListState(
    val stateType: ListStateType,
    val news: List<FeedItem>
)

class RssFeedViewModel(

    private val _newsService: FeedItemsService

) : ViewModel() {

    private val _state = MutableLiveData<FeedListState>()
    val state: MutableLiveData<FeedListState> = _state

    private val _listener : NewsListener = { news ->
        _state.value = _state.value?.copy(
            stateType = ListStateType.NORMAL,
            news = news
        )
    }

    init {
        _state.value = FeedListState(
            stateType = ListStateType.LOADING,
            news = emptyList()
        )
        loadNews()
    }

    override fun onCleared() {
        _newsService.removeListener(_listener)
        super.onCleared()
    }

    private fun loadNews() {
        _newsService.addListener(_listener)
    }

    private fun refresh() {
        _newsService.refresh()
    }
}