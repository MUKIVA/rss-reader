package com.mukiva.rssreader.addrss.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.App
import com.mukiva.rssreader.addrss.presentation.AddRssViewModel

class SearchViewModelFactory(
    private val _app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when ( modelClass ) {
            AddRssViewModel::class.java -> {
                AddRssViewModel(_app.SearchService, _app.feedsService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

fun Fragment.factory() = SearchViewModelFactory(requireContext().applicationContext as App)
