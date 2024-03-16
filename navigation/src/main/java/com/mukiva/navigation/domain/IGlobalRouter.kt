package com.mukiva.navigation.domain

import kotlinx.coroutines.flow.Flow

interface IGlobalRouter {

    val navigateEventFlow: Flow<INavigateEvent>

    suspend fun navigateTo(destination: IDestination)
    suspend fun navigateTo(
        destination: IDestination.IArgumentDestination,
        args: Collection<Any>
    )
    suspend fun navigateUp()

}