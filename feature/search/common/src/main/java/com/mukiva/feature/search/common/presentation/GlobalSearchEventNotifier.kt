package com.mukiva.feature.search.common.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object GlobalSearchEventNotifier {

    val eventFlow by lazy {  mEventFlow.asSharedFlow() }

    private val mEventFlow = MutableSharedFlow<Pair<SearchSateHolder, ISearchEvent>>()

    suspend fun notify(notifier: SearchSateHolder, event: ISearchEvent) {
        mEventFlow.emit(notifier to event)
    }

}