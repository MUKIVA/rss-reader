package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.watchfeeds.data.IFeedsService
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch
import kotlin.Exception

enum class NewsListStateType {
    LOADING,
    EMPTY,
    FAIL,
    NORMAL
}

data class NewsListState(
    val stateType: NewsListStateType,
    val news: List<News>
)

class NewsListViewModel(
    private val _feedsService: IFeedsService,
) : ViewModel() {
    private val _state = MutableLiveData<NewsListState>()
    val state: MutableLiveData<NewsListState> = _state

    init {
        _state.value = NewsListState(
            stateType = NewsListStateType.LOADING,
            news = emptyList()
        )
    }

    fun loadData(index: Int) = viewModelScope.launch {
        val news = _feedsService.getNewsByIndex(index)
        _state.value = NewsListState(
            stateType = getStateType(news),
            news = news
        )
    }

    fun refresh(index: Int) = viewModelScope.launch {
        try {
            _state.value = _state.value!!.copy(stateType = NewsListStateType.LOADING)
            val news = _feedsService.refreshNews(index)
            val stateType = getStateType(news)
            _state.value = NewsListState(stateType, news)
        } catch (e: Exception) {
            _state.value = NewsListState(NewsListStateType.FAIL, mutableListOf())
        }
    }

    private fun getStateType(news: MutableList<News>): NewsListStateType {
        return when (news.size) {
            0 -> NewsListStateType.EMPTY
            else -> NewsListStateType.NORMAL
        }
    }
}