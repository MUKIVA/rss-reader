package com.mukiva.rssreader.watchfeeds.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.rssreader.R
import com.mukiva.rssreader.components.FeedInfoBottomSheetDialog
import com.mukiva.rssreader.databinding.FragmentWatchFeedsBinding
import com.mukiva.rssreader.watchfeeds.di.factory
import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.presentation.FeedEvents
import com.mukiva.rssreader.watchfeeds.presentation.FeedState
import com.mukiva.rssreader.watchfeeds.presentation.FeedStateType
import com.mukiva.rssreader.watchfeeds.presentation.FeedListViewModel

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

class FeedListFragment : Fragment(R.layout.fragment_watch_feeds) {
    private lateinit var _binding: FragmentWatchFeedsBinding
    private val _viewModel: FeedListViewModel by viewModels { factory() }
    private lateinit var _adapter: NewsListFragmentAdapter
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
        _adapter = NewsListFragmentAdapter(this)
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        lifecycleScope.launchWhenStarted {
            _viewModel.eventFlow.collect { event ->
                when (event) {
                    is FeedEvents.DeleteRssEvent -> showDeleteAlertDialog(event.feed)
                    is FeedEvents.AddRssEvent -> findNavController().navigate(R.id.action_watchFeedsFragment_to_addRssFragment)
                    is FeedEvents.ShowToastEvent -> Toast.makeText(requireContext(), event.msgId, Toast.LENGTH_SHORT).show()
                    is FeedEvents.ShowFeedDetails -> showAboutFeedDialog(event.feed)
                }
            }
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
        _binding.feedLoading.root.isVisible = true
    }

    private fun renderNormalState() {
        initMenu()
        _binding.feedLoading.root.isVisible = false
        _binding.feedEmpty.root.isVisible = false
        _adapter.fragments = _viewModel.state.value?.feeds ?: emptyList()
    }

    private fun renderEmptyState() {
        hideMenu()
        _binding.feedLoading.root.isVisible = false
        _binding.tabLayout.isVisible = false
        _binding.feedViewPager.isVisible = false
        _binding.feedEmpty.root.isVisible = true
        _adapter.fragments = _viewModel.state.value?.feeds ?: emptyList()
    }

    private fun initPager() {
        _binding.feedViewPager.adapter = _adapter
        TabLayoutMediator(_binding.tabLayout, _binding.feedViewPager) { tab, pos ->
            tab.text = _viewModel.state.value?.feeds?.get(pos)?.title
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
        _viewModel.triggerDeleteFeed(_binding.tabLayout.selectedTabPosition)
        return true
    }

    private fun addRssFeed() : Boolean {
        _viewModel.triggerAddFeed()
        return true
    }

    private fun aboutFeed() : Boolean {
        _viewModel.triggerAboutFeedDialog(_binding.feedViewPager.currentItem)
        return true
    }

    private fun showAboutFeedDialog(item: Feed) {
        val ctx = requireContext()
        val dialog = BottomSheetDialog(ctx)
        val view = FeedInfoBottomSheetDialog(ctx)
        view.setTitle(item.title)
        view.setDescription(item.description)
        view.setOpenOriginalAction {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.newsRepoLink))
            startActivity(browserIntent)
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showDeleteAlertDialog(item: Feed) {
        val builder = AlertDialog.Builder(requireContext())
        val message = getString(R.string.delete_alert_dialog_message, item.title)
        builder.setMessage(message)
        builder.setPositiveButton(
            R.string.delete_alert_dialog_positive) { _, r -> handleDialogResponse(r) }
        builder.setNegativeButton(
            R.string.delete_alert_dialog_negative) { _, r -> handleDialogResponse(r) }
        builder.create().show()
    }

    private fun handleDialogResponse(response: Int) {
        when (response) {
            DialogInterface.BUTTON_POSITIVE ->
                _viewModel.deleteFeed(_binding.feedViewPager.currentItem)
        }
    }
}