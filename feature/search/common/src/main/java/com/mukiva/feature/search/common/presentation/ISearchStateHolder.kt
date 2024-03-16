package com.mukiva.feature.search.common.presentation

import com.mukiva.core.utils.domain.IResultError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface ISearchStateHolder {

    val searchStateFlow: StateFlow<SearchState>

    fun updateValue(value: String)
    fun search(url: String, scope: CoroutineScope)
    fun addRss(
        scope: CoroutineScope,
        onSuccess: suspend () -> Unit,
        onError: suspend (IResultError) -> Unit
    )

    companion object {
        const val SEARCH_DEBOUNCE = 1000L
    }
}