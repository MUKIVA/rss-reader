package com.mukiva.rssreader.addrss.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.addrss.domain.SearchException
import com.mukiva.rssreader.addrss.domain.SearchException.*
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.FeedsService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
class AddRssViewModel(
    private val _searchRssService: SearchRssService,
    private val _feedsService: FeedsService
) : SingleStateViewModel<AddRssState>(
    AddRssState(
        stateType = AddRssStateType.NORMAL,
        errorMessage = null,
        rssItem = null
    )
) {

    private val _searchDebounce = MutableSharedFlow<String>()

    companion object {
        private const val TIME_TO_SEARCH: Long = 1000
    }

    init {

        _searchDebounce
            .debounce(TIME_TO_SEARCH)
            .onEach { search(it) }
            .launchIn(viewModelScope)

    }

    suspend fun triggerSearch(text: String) {
        _searchDebounce.emit(text)
    }

    fun addRss() = viewModelScope.launch {
        _feedsService.addFeed(getState().rssItem!!)
    }

    private suspend fun search(link: String) {

        if (link.isEmpty()) return

        try {
            modifyState { getState().copy(stateType = AddRssStateType.SEARCH) }
            val feed = _searchRssService.search(link)
            modifyState(getState().copy(
                stateType = AddRssStateType.SEARCH_SUCCESS,
                rssItem = feed
            ))
        } catch (e: SearchException) {
            when (e) {
                is InvalidUrlException -> handleInvalidUrl()
                is TimeOutException -> handleTimeoutError()
                is ConnectionException -> handleConnectionError()
                is BackendException -> handleBackendError()
            }
        }
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
}