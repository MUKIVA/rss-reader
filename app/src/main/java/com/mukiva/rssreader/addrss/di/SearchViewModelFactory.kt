package com.mukiva.rssreader.addrss.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.App
import com.mukiva.rssreader.addrss.presentation.AddRssViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class SearchViewModelFactory(
    private val _app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when ( modelClass ) {
            AddRssViewModel::class.java -> {
                AddRssViewModel(_app.searchService, _app.feedsService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

@FlowPreview
fun Fragment.factory() = SearchViewModelFactory(requireContext().applicationContext as App)
