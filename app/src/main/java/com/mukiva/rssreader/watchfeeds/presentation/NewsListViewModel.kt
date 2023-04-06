package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.domain.*
import com.mukiva.rssreader.utils.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import com.mukiva.rssreader.watchfeeds.domain.News
import com.mukiva.rssreader.watchfeeds.domain.RefreshChannelUseCase
import kotlinx.coroutines.launch

class NewsListViewModel(
    val id: Long,
    private val _rssStorage: RssStorage,
    private val _rssSearchService: RssSearchGateway
) : SingleStateViewModel<NewsListState, NewsListEvents>(
    NewsListState(
        id = id,
        stateType = NewsListStateType.LOADING,
        news = listOf()
    )
) {

    suspend fun loadData() {
        val state = getState()
        val news = _rssStorage.getItems(state.id).map {
            itemToNews(it)
        }
        modifyState(state.copy(
            stateType = getStateType(news),
            news = news
        ))
    }

    fun refresh() {
        viewModelScope.launch {
            val id = getState().id

            when (val result = RefreshChannelUseCase(_rssStorage, _rssSearchService).invoke(id)) {
                is Error -> handleErrorRefresh(result.error)
                is Success<Channel> -> handleSuccessRefresh(result.data)
            }
        }
    }

    private fun itemToNews(item: Item): News {
        return News(
            id = item.id,
            title = item.title ?: "Undefined",
            description =  item.description ?: "Undefined",
            date = item.pubDate,
            imageLink = item.enclosure?.url,
            originalLink = item.link ?: ""
        )
    }

    private suspend fun handleSuccessRefresh(channel: Channel) {
        val news = channel.items.map { itemToNews(it) }
        modifyState(getState().copy(
            stateType = getStateType(news),
            news = news
        ))
        event(NewsListEvents.RefreshSuccessEvent)
    }

    private suspend fun handleErrorRefresh(err: SearchError) {
        when (err) {
            ConnectionError -> handleConnectionError()
            TimeoutError -> handleTimeoutError()
            else -> handleUnknownError()
        }
    }

    private suspend fun handleUnknownError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.refresh_unknown_error))
    }

    private suspend fun handleTimeoutError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.search_time_out_error))
    }

    private suspend fun handleConnectionError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.search_error_network))
    }

    private suspend fun setErrorState(nonEmptyEvt: NewsListEvents) {
        if (getState().news.isEmpty()) {
            modifyState(getState().copy(
                stateType = NewsListStateType.FAIL
            ))
        }

        event(nonEmptyEvt)
    }

    private fun getStateType(news: List<News>): NewsListStateType {
        return when (news.size) {
            0 -> NewsListStateType.EMPTY
            else -> NewsListStateType.NORMAL
        }
    }
}