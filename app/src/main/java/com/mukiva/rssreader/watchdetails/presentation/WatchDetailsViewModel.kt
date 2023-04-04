package com.mukiva.rssreader.watchdetails.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WatchDetailsViewModel(
    private val _rssStore: RssStorage
) : SingleStateViewModel<WatchDetailsState, Nothing>(
    WatchDetailsState(
        stateType = WatchDetailsStateType.LOADING
    )
) {
    private val _formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun setNews(id: Long) {
        viewModelScope.launch {
            modifyState(getState().copy(stateType = WatchDetailsStateType.LOADING))
            val news = _rssStore.getItem(id)
            modifyState(getState().copy(
                stateType = WatchDetailsStateType.NORMAL,
                title = news.title ?: "",
                description = news.description ?: "",
                imageLink = news.enclosure?.url ?: "",
                dateString = news.pubDate?.let { _formatter.format(it) } ?: "",
                originalLink = news.link ?: ""
            ))
        }
    }
}