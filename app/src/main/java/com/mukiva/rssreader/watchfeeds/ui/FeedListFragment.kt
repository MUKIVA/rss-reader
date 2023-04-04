@file:OptIn(FlowPreview::class)

package com.mukiva.rssreader.watchfeeds.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.ui.AddRssFragment
import com.mukiva.rssreader.databinding.FragmentWatchFeedsBinding
import com.mukiva.rssreader.watchfeeds.di.factory
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary
import com.mukiva.rssreader.watchfeeds.presentation.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
class FeedListFragment : Fragment(R.layout.fragment_watch_feeds) {
    private val _viewModel: FeedListViewModel by viewModels { factory() }
    private lateinit var _binding: FragmentWatchFeedsBinding
    private lateinit var _adapter: NewsListFragmentAdapter

    private val _menuProvider = WatchFeedsMenuProvider(
        object : WatchFeedsMenuProviderActions {

            override fun deleteFeed() {
                _viewModel.triggerDeleteFeed(_binding.tabLayout.selectedTabPosition)
            }

            override fun addFeed() {
                _viewModel.triggerAddFeed()
            }

            override fun aboutFeed() {
                _viewModel.triggerAboutFeedDialog(_binding.feedViewPager.currentItem)
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields(view)
        observeViewModel()
        initPager()

        render(_viewModel.state.value!!)
    }

    override fun onResume() {
        super.onResume()
        initActions()
        _viewModel.loadFeeds()
    }

    private fun initActions() {
        with (_binding.feedEmpty.addRssFeed.getFragment<AddRssFragment>()) {
            setBtnListener {
                getViewModel().addRss()
                _viewModel.loadFeeds()
        } }
    }

    private fun initFields(view: View) {
        _binding = FragmentWatchFeedsBinding.bind(view)
        _adapter = NewsListFragmentAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)
        collectEventFlow()
    }

    private fun collectEventFlow() {
        _viewModel.event
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleSingleEvents(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleSingleEvents(event: FeedEvents) {
        when (event) {
            is FeedEvents.DeleteRssEvent -> showDeleteAlertDialog(event.feed)
            is FeedEvents.AddRssEvent -> findNavController().navigate(R.id.action_watchFeedsFragment_to_addRssFragment)
            is FeedEvents.ShowToastEvent -> Toast.makeText(requireContext(), event.msgId, Toast.LENGTH_SHORT).show()
            is FeedEvents.ShowFeedDetails -> showAboutFeedDialog(event.feed)
        }
    }
    private fun render(state: FeedState) {
        when (state.stateType) {
            FeedStateType.LOADING -> renderLoadingState()
            FeedStateType.NORMAL -> renderNormalState(state)
            FeedStateType.EMPTY -> renderEmptyState(state)
        }
    }

    private fun renderLoadingState() {
        hideMenu()
        _binding.feedLoading.root.isVisible = true
        _binding.feedEmpty.root.isVisible = false
        _binding.tabLayout.isVisible = false
        _binding.feedViewPager.isVisible = false
    }

    private fun renderNormalState(state: FeedState) {
        initMenu()
        _binding.feedLoading.root.isVisible = false
        _binding.feedEmpty.root.isVisible = false
        _binding.feedViewPager.isVisible = true
        _binding.tabLayout.isVisible = true
        _adapter.feedSummaries = state.feeds.toList()
    }

    private fun renderEmptyState(state: FeedState) {
        hideMenu()
        _binding.feedLoading.root.isVisible = false
        _binding.tabLayout.isVisible = false
        _binding.feedViewPager.isVisible = false
        _binding.feedEmpty.root.isVisible = true
        _adapter.feedSummaries = state.feeds.toList()
    }

    private fun initPager() {
        with (_binding.feedViewPager) {
            adapter = _adapter

            TabLayoutMediator(_binding.tabLayout, _binding.feedViewPager) { tab, pos ->
                tab.text = _viewModel.state.value?.feeds?.get(pos)?.title
            }.attach()
        }
    }

    private fun initMenu() {
        hideMenu()
        (requireActivity() as MenuHost)
            .addMenuProvider(_menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun hideMenu() {
        (requireActivity() as MenuHost).removeMenuProvider(_menuProvider)
    }

    private fun showAboutFeedDialog(item: FeedSummary) {
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

    private fun showDeleteAlertDialog(item: FeedSummary) {
        val builder = AlertDialog.Builder(requireContext())
        val message = getString(R.string.delete_alert_dialog_message, item.title)
        builder.setMessage(message)
        builder.setPositiveButton(
            R.string.delete_alert_dialog_positive) { _, r -> handleDialogResponse(r, item) }
        builder.setNegativeButton(
            R.string.delete_alert_dialog_negative) { _, r -> handleDialogResponse(r, item) }
        builder.create().show()
    }

    private fun handleDialogResponse(response: Int, item: FeedSummary) {
        when (response) {
            DialogInterface.BUTTON_POSITIVE -> {
                _viewModel.deleteFeed(item.id)
            }
        }
    }
}