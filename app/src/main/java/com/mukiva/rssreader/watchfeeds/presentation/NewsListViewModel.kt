package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch
import com.mukiva.rssreader.addrss.domain.*

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

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val state = getState()
            val news = _rssStorage.getItems(state.id).map {
                itemToNews(it)
            }
            modifyState(state.copy(
                stateType = getStateType(news),
                news = news
            ))
        }
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
            title = item.title ?: "Undefined",
            description =  item.description ?: "Undefined",
            date = item.pubDate,
            imageLink = item.enclosure?.url,
            originalLink = item.link ?: ""
        )
    }

    private fun handleSuccessRefresh(channel: Channel) {
        val news = channel.items.map { itemToNews(it) }
        modifyState(getState().copy(
            stateType = getStateType(news),
            news = news
        ))
    }

    private fun handleErrorRefresh(err: SearchError) {
        when (err) {
            ConnectionError -> handleConnectionError()
            TimeoutError -> handleTimeoutError()
            else -> handleUnknownError()
        }
    }

    private fun handleUnknownError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.refresh_unknown_error))
    }

    private fun handleTimeoutError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.search_time_out_error))
    }

    private fun handleConnectionError() {
        setErrorState(NewsListEvents.RefreshErrorEvent(R.string.search_error_network))
    }

    private fun setErrorState(nonEmptyEvt: NewsListEvents) {
        viewModelScope.launch {
            if (getState().news.isEmpty()) {
                modifyState(getState().copy(
                    stateType = NewsListStateType.FAIL
                ))
            }

            event(nonEmptyEvt)
        }
    }

    private fun getStateType(news: List<News>): NewsListStateType {
        return when (news.size) {
            0 -> NewsListStateType.EMPTY
            else -> NewsListStateType.NORMAL
        }
    }
}