package com.mukiva.feature.search.common.presentation

sealed interface ISearchEvent {
    data object AddEvent : ISearchEvent
}