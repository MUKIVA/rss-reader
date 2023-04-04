package com.mukiva.rssreader.watchfeeds.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mukiva.rssreader.App
import com.mukiva.rssreader.watchfeeds.presentation.FeedListViewModel
import com.mukiva.rssreader.watchfeeds.presentation.NewsListViewModel

class FeedViewModelFactory(
    private val _app: App,
    private val _args: List<Any>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when ( modelClass ) {
            NewsListViewModel::class.java -> {
                NewsListViewModel(_args[0] as Long, _app.rssStorage, _app.searchGateway)
            }
            FeedListViewModel::class.java -> {
                FeedListViewModel(_app.rssStorage)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }

        return viewModel as T
    }
}

fun Fragment.factory(vararg args: Any) = FeedViewModelFactory(
    requireContext().applicationContext as App,
    args.toList()
    )