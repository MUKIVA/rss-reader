package com.mukiva.rssreader.addrss.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.addrss.domain.Feed
import com.mukiva.rssreader.addrss.domain.SearchException
import com.mukiva.rssreader.addrss.domain.SearchException.*
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
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
) : SingleStateViewModel<AddRssState>(
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
        handleAddRss()
    }

    private fun handleAddRss() = viewModelScope.launch {
        _rssService.addRss(_currentRss)
    }

    private suspend fun search(link: String) {
        if (link.isEmpty()) return
        var url = link.lowercase()
        if (!link.matches(Regex("^(https://).*$"))) url = "https://$link"

        try {
            modifyState { getState().copy(stateType = AddRssStateType.SEARCH) }
            _currentRss = _searchRssService.search(url)

            if  (_rssService.getAllRss().firstOrNull {
                it.title == _currentRss.channel.title
            } != null)
                throw SearchException.RssAlreadyAdded()

            modifyState(getState().copy(
                stateType = AddRssStateType.SEARCH_SUCCESS,
                rssItem = ConverRssToFeed(_currentRss)
            ))
        } catch (e: SearchException) {
            when (e) {
                is InvalidUrlException -> handleInvalidUrl()
                is TimeOutException -> handleTimeoutError()
                is ConnectionException -> handleConnectionError()
                is BackendException -> handleBackendError()
                is RssAlreadyAdded -> handleRssAlreadyAdded()
            }
        }
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

    private fun handleBackendError() {
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