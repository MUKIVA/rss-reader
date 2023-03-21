package com.mukiva.rssreader.addrss.presentation

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.SearchRssService
import com.mukiva.rssreader.watchfeeds.data.FeedsService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

enum class AddRssStateType {
    NORMAL,
    SEARCH,
    SEARCH_FAIL,
    SEARCH_SUCCESS
}

data class AddRssState (
    val stateType: AddRssStateType,
    val errorMessage: Int,
    val rssItem: Feed?
)

class AddRssViewModel(
    private val _searchRssService: SearchRssService,
    private val _feedsService: FeedsService
) : ViewModel() {
    private val _state = MutableLiveData<AddRssState>()
    val state: MutableLiveData<AddRssState> = _state
    private var _timer: CountDownTimer? = null
    private val _timerTime: Long = 15000
    private val _startSearchTime: Long = 14000
    private val _timerInterval: Long = 1
    private var _scope: CoroutineScope? = null

    init {
        _state.value = AddRssState(
            stateType = AddRssStateType.NORMAL,
            errorMessage = R.string.search_time_out_error,
            rssItem = null
        )
    }

    fun triggerSearch(text: String) {
        cancelSearch()
        if (text.isEmpty()) return
        _timer = object : CountDownTimer(_timerTime, _timerInterval) {
            override fun onTick(currentTime: Long) {
                if (currentTime <= _startSearchTime && _state.value!!.stateType != AddRssStateType.SEARCH)
                    search(text)
            }
            override fun onFinish() { timeOutError() }
        }
        _timer?.start()
    }

    private fun cancelSearch() {
        _timer?.cancel()
        _scope?.cancel()
        _state.value = _state.value!!.copy( stateType = AddRssStateType.NORMAL)
    }

    private fun timeOutError() {
        cancelSearch()
        _state.value = AddRssState(
            stateType = AddRssStateType.SEARCH_FAIL,
            errorMessage = R.string.search_time_out_error,
            rssItem = null
        )
    }

    private fun search(link: String) = viewModelScope.launch {
        _scope?.cancel()
        _scope = this
        try {
            _state.value = _state.value!!.copy( stateType = AddRssStateType.SEARCH)
            val feed = _searchRssService.search(link)
            _state.value = _state.value!!.copy(
                stateType = AddRssStateType.SEARCH_SUCCESS,
                rssItem = feed
            )
        } catch (e: Exception) {
            _state.value = _state.value!!.copy(
                stateType = AddRssStateType.SEARCH_FAIL,
                errorMessage = R.string.search_error_network
            )
        } finally {
            _timer?.cancel()
        }
    }

    fun addRss() = viewModelScope.launch {
        _feedsService.addFeed(_state.value!!.rssItem!!)
    }
}