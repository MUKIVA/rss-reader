package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.FeedsService
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch
import kotlin.Exception

class NewsListViewModel(
    private val _feedsService: FeedsService,
) : SingleStateViewModel<NewsListState>(
    NewsListState(
        stateType = NewsListStateType.LOADING,
        news = listOf()
    )
) {

    fun loadData(index: Int) = viewModelScope.launch {
        val news = _feedsService.getNewsByIndex(index)
        modifyState(NewsListState(
            stateType = getStateType(news),
            news = news
        ))
    }

    fun refresh(index: Int) = viewModelScope.launch {
        try {
            modifyState { copy(stateType = NewsListStateType.LOADING) }
            val news = _feedsService.refreshNews(index)
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