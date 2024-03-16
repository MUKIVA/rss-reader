package com.mukiva.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mukiva.navigation.domain.IDestinationsHolder
import com.mukiva.navigation.domain.IGlobalRouter
import com.mukiva.navigation.domain.INavigateEvent

@Composable
fun StatefulNavigationLayer(
    destinationsHolder: IDestinationsHolder,
    globalRouter: IGlobalRouter,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val events = globalRouter
          .navigateEventFlow
          .collectAsState(initial = null)
    val event = events.value

    LaunchedEffect(event) {
        event?.run {
            when(event) {
                is INavigateEvent.INavigateToDestinationEvent -> {
                    navController.navigate(event.route)
                }
                is INavigateEvent.INavigateUpEvent -> {
                    navController.navigateUp()
                }
                is INavigateEvent.INavigateToDestinationWithArgsEvent -> {
                    val route = buildString {
                        append(event.route)
                        event.args.forEach {
                            append("/$it")
                        }
                    }
                    navController.navigate(route)
                }
            }
        }
    }

    StatelessNavigationLayer(
        destinationsHolder = destinationsHolder,
        navController = navController,
        modifier = modifier
    )
}