package com.mukiva.navigation.domain

import androidx.compose.runtime.Composable
import androidx.navigation.NavType

sealed interface IDestination {
    val route: String

    interface IEmptyArgsDestination : IDestination {
        val screen: @Composable () -> Unit
    }

    interface IArgumentDestination : IDestination {
        val argsType: Collection<Pair<String, NavType<*>>>
        val screen: @Composable (List<Pair<String, Any?>>?) -> Unit
    }
}

