package com.mukiva.rssreader.watchdetails.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mukiva.rssreader.watchfeeds.domain.News

enum class WatchDetailsStateType {
    NORMAL,
    PARSE_ERROR
}

data class WatchDetailsState(
    val stateType: WatchDetailsStateType,
)

class WatchDetailsViewModel : ViewModel() {
    private val _state = MutableLiveData<WatchDetailsState>()
    val state: MutableLiveData<WatchDetailsState> = _state

    init {
        _state.value = WatchDetailsState(
            stateType = WatchDetailsStateType.NORMAL
        )
    }

    fun parseRssItem(item: News) {

    }
}