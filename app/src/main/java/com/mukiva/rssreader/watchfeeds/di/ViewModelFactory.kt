package com.mukiva.rssreader.watchfeeds.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.App
import com.mukiva.rssreader.watchfeeds.presentation.FeedListViewModel
import com.mukiva.rssreader.watchfeeds.presentation.NewsListViewModel

class ViewModelFactory(
    private val _app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when ( modelClass ) {
            NewsListViewModel::class.java -> {
                NewsListViewModel(_app.feedsService)
            }
            FeedListViewModel::class.java -> {
                FeedListViewModel(_app.feedsService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)