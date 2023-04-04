package com.mukiva.rssreader.watchdetails.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.App
import com.mukiva.rssreader.watchdetails.presentation.WatchDetailsViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class WatchDetailsViewModelFactory(
    private val _app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when ( modelClass ) {
            WatchDetailsViewModel::class.java -> {
                WatchDetailsViewModel(_app.rssStorage)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

@FlowPreview
fun Fragment.factory() = WatchDetailsViewModelFactory(requireContext().applicationContext as App)
