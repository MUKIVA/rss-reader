package com.mukiva.rssreader.watchfeeds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentRssFeedBinding
import com.mukiva.rssreader.watchfeeds.di.factory
import com.mukiva.rssreader.watchfeeds.domain.FeedItem
import com.mukiva.rssreader.watchfeeds.presentation.FeedListState
import com.mukiva.rssreader.watchfeeds.presentation.RssFeedViewModel
import com.mukiva.rssreader.watchfeeds.presentation.ListStateType

class RssFeedFragment : Fragment(R.layout.fragment_rss_feed) {

    private lateinit var _binding: FragmentRssFeedBinding
    private lateinit var _adapter: RssFeedItemsAdapter
    private val _viewModel: RssFeedViewModel by viewModels { factory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        initRecyclerView()
        observeViewModel()
    }

    private fun initFields(view: View) {
        _binding = FragmentRssFeedBinding.bind(view)

        _adapter = RssFeedItemsAdapter(object: FeedItemActions {
            override fun onItemDetails(item: FeedItem) {
                findNavController().navigate(R.id.action_watchFeedsFragment_to_watchDetailsFragment)
            }
        })
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        _binding.recyclerView.layoutManager = layoutManager
        _binding.recyclerView.adapter = _adapter
    }

    private fun render(state: FeedListState) {
        when (state.stateType) {
            ListStateType.EMPTY -> renderEmptyState()
            ListStateType.LOADING -> renderLoadingState()
            ListStateType.NORMAL -> renderNormalState(state)
            ListStateType.FAIL -> renderFailState()
        }
    }

    private fun renderEmptyState() {
        with(_binding.emptyView) {

            feedListEmptyImage.visibility = View.VISIBLE

            feedListEmptyText.text = resources.getString(R.string.empty_rss_feed)
            feedListEmptyText.visibility = View.VISIBLE

            feedListEmptyButton.text = resources.getString(R.string.update)
            feedListEmptyButton.visibility = View.VISIBLE

            feedListEmptyProgress.visibility = View.GONE
        }
        _binding.emptyView.root.visibility = View.VISIBLE
    }

    private fun renderLoadingState() {
        with(_binding.emptyView) {

            feedListEmptyImage.visibility = View.GONE

            feedListEmptyText.text = resources.getString(R.string.loading_text)
            feedListEmptyText.visibility = View.VISIBLE

            feedListEmptyButton.visibility = View.GONE

            feedListEmptyProgress.visibility = View.VISIBLE
        }
        _binding.emptyView.root.visibility = View.VISIBLE
    }

    private fun renderNormalState(state: FeedListState) {
        _binding.emptyView.root.visibility = View.GONE
        _adapter.items = state.news
    }

    private fun renderFailState() {
        with(_binding.emptyView) {

            feedListEmptyImage.visibility = View.VISIBLE

            feedListEmptyText.text = resources.getString(R.string.fail_get_data_text)
            feedListEmptyText.visibility = View.VISIBLE

            feedListEmptyButton.text = resources.getString(R.string.repeat)
            feedListEmptyButton.visibility = View.VISIBLE

            feedListEmptyProgress.visibility = View.GONE
        }
        _binding.emptyView.root.visibility = View.VISIBLE
    }
}