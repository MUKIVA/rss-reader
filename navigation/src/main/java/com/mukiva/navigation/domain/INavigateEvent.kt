package com.mukiva.navigation.domain

sealed interface INavigateEvent {
    interface INavigateToDestinationEvent : INavigateEvent {
        val route: String
    }
    interface INavigateToDestinationWithArgsEvent : INavigateEvent {
        val route: String
        val args: Collection<Any>
    }
    interface INavigateUpEvent : INavigateEvent
}