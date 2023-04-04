package com.mukiva.rssreader.addrss.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.domain.*
import com.mukiva.rssreader.addrss.domain.UnknownError
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
class AddRssViewModel(
    private val _searchGateway: RssSearchGateway,
    private val _rssStorage: RssStorage
) : SingleStateViewModel<AddRssState, Nothing>(
    AddRssState(
        stateType = AddRssStateType.NORMAL,
        errorMessage = null,
        rssItem = null
    )
) {
    private val _searchDebounce = MutableSharedFlow<String>()
    private var _currentRss: Rss? = null

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
        viewModelScope.launch {
            _searchDebounce.emit(text)
        }
    }

    fun addRss() {
        viewModelScope.launch {
            _rssStorage.add(_currentRss!!)
            _currentRss = null
            modifyState(getState().copy(
                stateType = AddRssStateType.NORMAL,
                rssItem = null,
                errorMessage = null
            ))
        }
    }

    private suspend fun search(link: String) {
        if (link.isEmpty()) return
        modifyState { getState().copy(stateType = AddRssStateType.SEARCH) }

        when (val result = SearchChannelUseCase(_searchGateway).invoke(link)) {
            is Success<Rss> -> handleSuccessSearch(result.data, result.data.refreshLink)
            is Error -> handleErrorSearch(result.error)
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

    private suspend fun handleSuccessSearch(rss: Rss, refreshLink: String) {
        _currentRss = rss.copy(refreshLink = refreshLink)

        val allrss = _rssStorage.getAllRss()
        if (allrss.firstOrNull { it.refreshLink.equals(_currentRss!!.refreshLink) } != null) {
            handleRssAlreadyAdded()
            return
        }

        modifyState(getState().copy(
            stateType = AddRssStateType.SEARCH_SUCCESS,
            rssItem = convertRssToFeed(_currentRss!!)
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

    private fun convertRssToFeed(rss: Rss): Feed {
        return Feed(
            title = rss.channel.title,
            description = rss.channel.description,
            imageLink = rss.channel.image?.url
        )
    }
}