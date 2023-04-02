package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.converters.RssConverter
import com.mukiva.rssreader.watchfeeds.data.RssService
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch
import com.mukiva.rssreader.addrss.domain.*

class NewsListViewModel(
    private val _rssService: RssService,
    private val _rssSearchService: SearchRssService
) : SingleStateViewModel<NewsListState, NewsListEvents>(
    NewsListState(
        stateType = NewsListStateType.LOADING,
        news = listOf()
    )
) {
    private val converter = RssConverter()

    fun loadData(index: Int) { viewModelScope.launch {
        val rss = _rssService.getAllRss()[index]
        val news = _rssService.getChannelItems(rss).map {
            converter.entityToNews(it)
        }
        modifyState(NewsListState(
            stateType = getStateType(news),
            news = news
        ))
    } }

    fun refresh(index: Int) {
        viewModelScope.launch {
            val entity = _rssService.getAllRss()[index]

            when (val result = _rssSearchService.search(entity.refreshLink)) {
                is Error -> handleErrorRefresh(result.error)
                is Success<Rss> -> handleSuccessRefresh(result.data, entity.id)
            }
        }
    }

    private suspend fun handleSuccessRefresh(rss: Rss, id: Long) {
        val entity = _rssService.updateRss(rss, id)
        val news = entity.items.map { converter.entityToNews(it) }
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