package com.mukiva.rssreader.glue.navigation

import com.mukiva.navigation.domain.IDestination
import com.mukiva.navigation.domain.IGlobalRouter
import com.mukiva.navigation.domain.INavigateEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalRouter @Inject constructor() : IGlobalRouter {
    override val navigateEventFlow: Flow<INavigateEvent>
        get() = mNavigateEventFow

    private val mNavigateEventFow = MutableSharedFlow<INavigateEvent>()

    override suspend fun navigateTo(destination: IDestination) {
        mNavigateEventFow.emit(object : INavigateEvent.INavigateToDestinationEvent {
            override val route: String
                get() = destination.route

        })
    }

    override suspend fun navigateTo(
        destination: IDestination.IArgumentDestination,
        args: Collection<Any>
    ) {
        mNavigateEventFow.emit(object : INavigateEvent.INavigateToDestinationWithArgsEvent {
            override val route: String
                get() = destination.route
            override val args: Collection<Any>
                get() = args

        })
    }

    override suspend fun navigateUp() {
        mNavigateEventFow.emit(object : INavigateEvent.INavigateUpEvent {})
    }
}