package com.mukiva.feature.search.impl.presentation

sealed interface ISearchEvent {

    data object NoConnection : ISearchEvent
    data object UnknownError : ISearchEvent
    data object SuccessAdd : ISearchEvent

}