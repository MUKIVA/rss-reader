package com.mukiva.rssreader.watchfeeds.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentWatchFeedsBinding
import com.mukiva.rssreader.watchfeeds.presentation.FeedState
import com.mukiva.rssreader.watchfeeds.presentation.FeedStateType
import com.mukiva.rssreader.watchfeeds.presentation.WatchFeedsViewModel

class FeedMenuProvider : MenuProvider {
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.feed_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }
}

class WatchFeedsFragment : Fragment(R.layout.fragment_watch_feeds) {

    private lateinit var _binding: FragmentWatchFeedsBinding
    private lateinit var _viewModel: WatchFeedsViewModel
    private lateinit var _adapter: RssFeedFragmentAdapter
    private val _menuProvider = FeedMenuProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
        observeViewModel()
        initPager()

//        initMenu()
    }

    private fun initFields(view: View) {
        _binding = FragmentWatchFeedsBinding.bind(view)
        _adapter = RssFeedFragmentAdapter(this)
        _viewModel = ViewModelProvider(this)[WatchFeedsViewModel::class.java]
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FeedState) {
        when (state.stateType) {
            FeedStateType.LOADING -> renderLoadingState()
            FeedStateType.NORMAL -> renderNormalState()
            FeedStateType.EMPTY -> renderEmptyState()
        }
    }

    private fun renderLoadingState() {
        _binding.feedLoading.root.visibility = View.VISIBLE
    }

    private fun renderNormalState() {
        _binding.feedLoading.root.visibility = View.GONE
        _binding.feedEmpty.root.visibility = View.GONE
        _adapter.fragments = _viewModel.state.value?.feeds ?: emptyList()
    }

    private fun renderEmptyState() {
        _binding.feedLoading.root.visibility = View.GONE
        _binding.feedEmpty.root.visibility = View.VISIBLE
    }

    private fun initPager() {
        _binding.feedViewPager.adapter = _adapter
        TabLayoutMediator(_binding.tabLayout, _binding.feedViewPager) { tab, pos ->
            tab.text = _viewModel.state.value?.feeds?.get(pos)?.rssTitle
        }.attach()
    }

    private fun initMenu() {
        (requireActivity() as MenuHost)
            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}