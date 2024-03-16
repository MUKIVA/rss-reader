package com.mukiva.feature.search.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.presentation.ISearchStateHolder
import com.mukiva.feature.search.impl.navigation.ISearchRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVewModel @Inject constructor(
    searchSateHolder: ISearchStateHolder,
    private val searchRouter: ISearchRouter
) : ViewModel(), ISearchStateHolder by searchSateHolder {

    val eventFlow by lazy { mEventFlow.asSharedFlow() }

    private val mEventFlow = MutableSharedFlow<ISearchEvent>()

    init {
        @Suppress("OPT_IN_USAGE")
        searchStateFlow
            .map { it.value }
            .debounce(ISearchStateHolder.SEARCH_DEBOUNCE)
            .onEach { search(it, viewModelScope) }
            .launchIn(viewModelScope)
    }

    fun addRss() {
        addRss(viewModelScope
            , onError = {
                when(it) {
                    IResultError.ConnectionError -> mEventFlow.emit(ISearchEvent.NoConnection)
                    else -> mEventFlow.emit(ISearchEvent.UnknownError)
                }
            }
            , onSuccess = {
                mEventFlow.emit(ISearchEvent.SuccessAdd)
            }
        )
    }

    fun goBack() {
        viewModelScope.launch {
            searchRouter.goBack()
        }
    }
}