package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.converters.RssConverter
import com.mukiva.rssreader.watchfeeds.data.RssService
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch
import kotlin.Exception

class NewsListViewModel(
    private val _rssService: RssService,
    private val _rssSearchService: SearchRssService
) : SingleStateViewModel<NewsListState>(
    NewsListState(
        stateType = NewsListStateType.LOADING,
        news = listOf()
    )
) {

    fun loadData(index: Int) = viewModelScope.launch {
        val rss = _rssService.getAllRss()[index]
        val news = _rssService.getChannelItems(rss).map {
            RssConverter.itemEntityToNewsConverter.convert(it)
        }
        modifyState(NewsListState(
            stateType = getStateType(news),
            news = news
        ))
    }

    fun refresh(index: Int) = viewModelScope.launch {
        val entity = _rssService.getAllRss()[index]
        try {
            modifyState { copy(stateType = NewsListStateType.LOADING) }
            val rss = _rssSearchService.search(entity.link)
            val news = _rssService.getChannelItems(_rssService.updateRss(rss, entity.id)).map {
                RssConverter.itemEntityToNewsConverter.convert(it)
            }
            val stateType = getStateType(news)
            modifyState { NewsListState(stateType, news) }
        } catch (e: Exception) {
            modifyState { NewsListState(NewsListStateType.FAIL, mutableListOf()) }
        }
    }

    private fun getStateType(news: List<News>): NewsListStateType {
        return when (news.size) {
            0 -> NewsListStateType.EMPTY
            else -> NewsListStateType.NORMAL
        }
    }
}