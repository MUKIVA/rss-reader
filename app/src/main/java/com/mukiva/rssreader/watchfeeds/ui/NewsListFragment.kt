package com.mukiva.rssreader.watchfeeds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentNewsListBinding
import com.mukiva.rssreader.watchfeeds.di.factory
import com.mukiva.rssreader.watchfeeds.domain.News
import com.mukiva.rssreader.watchfeeds.presentation.NewsListState
import com.mukiva.rssreader.watchfeeds.presentation.NewsListViewModel
import com.mukiva.rssreader.watchfeeds.presentation.NewsListStateType

class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private lateinit var _binding: FragmentNewsListBinding
    private lateinit var _adapter: NewsListItemAdapter
    private val _viewModel: NewsListViewModel by viewModels { factory() }
    private var _position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        initRecyclerView()
        initActions()
        initRefreshLayouts()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        reloadWhenResume()
    }

    fun setPosition(position: Int) {
        _position = position
    }

    private fun reloadWhenResume() = lifecycleScope.launchWhenResumed {
        _viewModel.loadData(_position)
    }

    private fun refresh() {
        _viewModel.refresh(_position)
    }

    private fun initActions() {
        _binding.emptyView.feedListEmptyButton.setOnClickListener { refresh() }
    }

    private fun initRefreshLayouts() {
        _binding.refreshLayout.setOnRefreshListener {
            refresh()
        }
    }

    private fun initFields(view: View) {
        _binding = FragmentNewsListBinding.bind(view)

        _adapter = NewsListItemAdapter(object: FeedItemEvent {
            override fun onItemDetails(item: News) {
                findNavController().navigate(R.id.action_watchFeedsFragment_to_watchDetailsFragment)
            }
        })
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        _binding.recyclerView.layoutManager = layoutManager
        _binding.recyclerView.adapter = _adapter
    }

    private fun render(state: NewsListState) {
        when (state.stateType) {
            NewsListStateType.EMPTY -> renderEmptyState()
            NewsListStateType.LOADING -> renderLoadingState()
            NewsListStateType.NORMAL -> renderNormalState(state)
            NewsListStateType.FAIL -> renderFailState()
        }
    }

    private fun renderEmptyState() {
        with(_binding.emptyView) {

            feedListEmptyImage.isVisible = true

            feedListEmptyText.text = resources.getString(R.string.empty_rss_feed)
            feedListEmptyText.isVisible = true

            feedListEmptyButton.text = resources.getString(R.string.update)
            feedListEmptyButton.isVisible = true

            feedListEmptyProgress.isVisible = false
        }
        _binding.emptyView.root.isVisible = true
        _binding.refreshLayout.isRefreshing = false
    }

    private fun renderLoadingState() {
        with(_binding.emptyView) {

            feedListEmptyImage.isVisible = false

            feedListEmptyText.text = resources.getString(R.string.loading_text)
            feedListEmptyText.isVisible = true

            feedListEmptyButton.isVisible = false

            feedListEmptyProgress.isVisible = true
        }
        _binding.recyclerView.isVisible = false
        _binding.emptyView.root.isVisible = true
    }

    private fun renderNormalState(state: NewsListState) {
        _binding.emptyView.root.isVisible = false
        _adapter.items = state.news
        _binding.refreshLayout.isRefreshing = false
        _binding.recyclerView.isVisible = true
    }

    private fun renderFailState() {
        with(_binding.emptyView) {

            feedListEmptyImage.isVisible = true

            feedListEmptyText.text = resources.getString(R.string.fail_get_data_text)
            feedListEmptyText.isVisible = true

            feedListEmptyButton.text = resources.getString(R.string.repeat)
            feedListEmptyButton.isVisible = true

            feedListEmptyProgress.isVisible = false
        }
        _binding.emptyView.root.isVisible = true
        _binding.refreshLayout.isRefreshing = false
        _binding.recyclerView.isVisible = false
    }
}