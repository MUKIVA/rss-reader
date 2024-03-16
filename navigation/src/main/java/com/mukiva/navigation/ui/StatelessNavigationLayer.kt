package com.mukiva.navigation.ui


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mukiva.navigation.domain.IDestination
import com.mukiva.navigation.domain.IDestinationsHolder

@Composable
fun StatelessNavigationLayer(
    destinationsHolder: IDestinationsHolder,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = destinationsHolder.startDestination.route,
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseInOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseInOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseInOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                animationSpec = tween(300, easing = EaseInOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        },
        modifier = modifier
    ) {
        destinationsHolder.destinations.forEach { destination ->
            when(destination) {
                is IDestination.IArgumentDestination -> {
                    val route = buildString {
                        append(destination.route)
                        destination.argsType.forEach { (name, _) ->
                            append("/{$name}")
                        }
                    }
                    composable(
                        route = route,
                        arguments = buildList {
                            destination.argsType.forEach { (name, type) ->
                                add(navArgument(
                                    name = name,
                                    builder = { this.type = type }
                                ))
                            }
                        }
                    ) { backStackEntry ->
                        val args = backStackEntry.arguments?.run {
                            destination.argsType.map { (name, type) ->
                                Pair(name, type[this, name])
                            }
                        }
                        destination.screen(args)
                    }
                }
                is IDestination.IEmptyArgsDestination -> composable(route = destination.route) {
                    destination.screen()
                }
            }

        }
    }
}

inline fun <reified T> List<Pair<String, T?>>?.getValue(key: String): T? {
    val (_, value) = this?.find { it.first == key } ?: error("Key $key is not contained")
    return value as T
}