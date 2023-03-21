package com.mukiva.rssreader.addrss.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mukiva.rssreader.watchfeeds.domain.Feed

enum class AddRssStateType {
    NORMAL,
    SEARCH,
    SEARCH_FAIL,
    SEARCH_SUCCESS
}

data class AddRssState (
    val stateType: AddRssStateType,
    val errorMessage: String,
    val searchText: String,
    val rssItem: Feed?
)

class AddRssViewModel : ViewModel() {

    private val _state = MutableLiveData<AddRssState>()
    val state: MutableLiveData<AddRssState> = _state

    init {
        _state.value = AddRssState(
            stateType = AddRssStateType.NORMAL,
            errorMessage = "",
            searchText = "",
            rssItem = null
        )
    }

    fun search() {
        throw NotImplementedError()
    }

    fun addRss() {
        throw NotImplementedError()
    }
}