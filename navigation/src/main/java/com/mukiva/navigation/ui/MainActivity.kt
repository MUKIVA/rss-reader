package com.mukiva.navigation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mukiva.navigation.domain.IDestinationsHolder
import com.mukiva.navigation.domain.IGlobalRouter
import com.mukiva.navigation.ui.theme.RSSTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var destinationsHolder: IDestinationsHolder

    @Inject
    lateinit var globalRouter: IGlobalRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RSSTheme {
                StatefulNavigationLayer(
                    destinationsHolder = destinationsHolder,
                    globalRouter = globalRouter
                )
            }
        }
    }

}