package com.mukiva.rssreader.watchfeeds.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentWatchFeedsBinding
import com.mukiva.rssreader.watchfeeds.presentation.FeedState
import com.mukiva.rssreader.watchfeeds.presentation.FeedStateType
import com.mukiva.rssreader.watchfeeds.presentation.WatchFeedsViewModel

typealias ProviderAction = () -> Boolean

class WatchFeedsMenuProvider(
    private var aboutFeed : ProviderAction,
    private var addFeed : ProviderAction,
    private var deleteFeed : ProviderAction
) : MenuProvider {


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.feed_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId)
        {
            R.id.about_feed -> return aboutFeed()
            R.id.add_feed -> return addFeed()
            R.id.delete_feed -> return deleteFeed()
        }
        return false
    }
}

class WatchFeedsFragment : Fragment(R.layout.fragment_watch_feeds) {

    private lateinit var _binding: FragmentWatchFeedsBinding
    private lateinit var _viewModel: WatchFeedsViewModel
    private lateinit var _adapter: RssFeedFragmentAdapter
    private val _menuProvider = WatchFeedsMenuProvider(
        aboutFeed = { aboutFeed() },
        addFeed = { addRssFeed() },
        deleteFeed = { deleteRss() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
        observeViewModel()
        initPager()
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
        hideMenu()
        _binding.feedLoading.root.visibility = View.VISIBLE
    }

    private fun renderNormalState() {
        initMenu()
        _binding.feedLoading.root.visibility = View.GONE
        _binding.feedEmpty.root.visibility = View.GONE
        _adapter.fragments = _viewModel.state.value?.feeds ?: emptyList()
    }

    private fun renderEmptyState() {
        hideMenu()
        _binding.feedLoading.root.visibility = View.GONE
        _binding.tabLayout.isVisible = false
        _binding.feedViewPager.isVisible = false
        _binding.feedEmpty.root.visibility = View.VISIBLE
        _adapter.fragments = _viewModel.state.value?.feeds ?: emptyList()
    }

    private fun initPager() {
        _binding.feedViewPager.adapter = _adapter
        TabLayoutMediator(_binding.tabLayout, _binding.feedViewPager) { tab, pos ->
            tab.text = _viewModel.state.value?.feeds?.get(pos)?.rssTitle
        }.attach()
    }

    private fun initMenu() {
        hideMenu()
        (requireActivity() as MenuHost)
            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun hideMenu() {
        (requireActivity() as MenuHost).removeMenuProvider(_menuProvider)
    }

    private fun deleteRss() : Boolean {
        _viewModel.deleteRssFeed(_binding.tabLayout.selectedTabPosition)
        return true
    }

    private fun addRssFeed() : Boolean {
        _viewModel.addRssFeed(findNavController(), requireContext())
        return true
    }

    private fun aboutFeed() : Boolean {
        _viewModel.showDetailsRssFeed(_binding.tabLayout.selectedTabPosition)
        return true
    }
}