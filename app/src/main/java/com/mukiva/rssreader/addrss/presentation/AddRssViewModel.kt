package com.mukiva.rssreader.addrss.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.domain.*
import com.mukiva.rssreader.addrss.domain.UnknownError
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
class AddRssViewModel(
    private val _searchRssService: SearchRssService,
    private val _rssService: RssService
) : SingleStateViewModel<AddRssState, Nothing>(
    AddRssState(
        stateType = AddRssStateType.NORMAL,
        errorMessage = null,
        rssItem = null
    )
) {
    private val _searchDebounce = MutableSharedFlow<String>()
    private var _currentRss: Rss = Rss()

    companion object {
        private const val TIME_TO_SEARCH: Long = 1000
    }

    init {
        _searchDebounce
            .debounce(TIME_TO_SEARCH)
            .onEach { search(it) }
            .launchIn(viewModelScope)

    }

    fun triggerSearch(text: String) {
        handleTriggerSearch(text)
    }

    fun addRss() {
        viewModelScope.launch {
            _rssService.addRss(_currentRss)
        }
    }

    private suspend fun search(link: String) {
        if (link.isEmpty()) return
        var url = link.lowercase()
        if (!link.matches(Regex("^(https://).*$"))) url = "https://$link"
        modifyState { getState().copy(stateType = AddRssStateType.SEARCH) }

        val newRss = _searchRssService.search(url)

        when (newRss) {
            is Success<Rss> -> handleSuccessSearch(newRss.data, url)
            is Error -> handleErrorSearch(newRss.error)
        }
    }

    private fun handleErrorSearch(err: SearchError) {
        when (err) {
            ConnectionError -> handleConnectionError()
            InvalidUrlError -> handleInvalidUrl()
            TimeoutError -> handleTimeoutError()
            UnknownError -> handleUnknownError()
        }
    }

    private fun handleSuccessSearch(rss: Rss, refreshLink: String) {
        _currentRss = rss.copy(refreshLink = refreshLink)
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_SUCCESS,
            rssItem = ConverRssToFeed(_currentRss)
        ))
    }

    private fun handleRssAlreadyAdded() {
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_already_added_error
        ))
    }

    private fun handleInvalidUrl() {
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_error_parse
        ))
    }

    private fun handleTimeoutError() {
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_time_out_error
        ))
    }

    private fun handleConnectionError() {
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_error_network
        ))
    }

    private fun handleUnknownError() {
        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_error_parse
        ))
    }

    private fun handleTriggerSearch(text: String) = viewModelScope.launch {
        _searchDebounce.emit(text)
    }

    private fun ConverRssToFeed(rss: Rss): Feed {
        return Feed(
            title = rss.channel.title,
            description = rss.channel.description,
            imageLink = rss.channel.image?.url
        )
    }
}