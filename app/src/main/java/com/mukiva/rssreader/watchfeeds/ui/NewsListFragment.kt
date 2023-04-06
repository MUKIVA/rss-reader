package com.mukiva.rssreader.watchfeeds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.FragmentNewsListBinding
import com.mukiva.rssreader.watchdetails.ui.NewsDetailsFragment
import com.mukiva.rssreader.watchfeeds.di.factory
import com.mukiva.rssreader.watchfeeds.domain.News
import com.mukiva.rssreader.watchfeeds.presentation.NewsListEvents
import com.mukiva.rssreader.watchfeeds.presentation.NewsListState
import com.mukiva.rssreader.watchfeeds.presentation.NewsListViewModel
import com.mukiva.rssreader.watchfeeds.presentation.NewsListStateType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
class NewsListFragment : Fragment(R.layout.fragment_news_list) {

    private var _position: Int = 0
    private var _id: Long = -1

    private lateinit var _binding: FragmentNewsListBinding
    private lateinit var _adapter: NewsListItemAdapter
    private val _viewModel: NewsListViewModel by viewModels { factory(_id) }
    private var _positionListener: ((Int) -> Unit)? = null



    companion object {
        private const val ARG_ID = "ARG_ID"
        private const val ARG_POSITION = "ARG_POS"

        fun newInstance(id: Long, position: Int) = NewsListFragment().apply {
            arguments = bundleOf(
                ARG_ID to id,
                ARG_POSITION to position
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _id = requireArguments().getLong(ARG_ID)
        _position = requireArguments().getInt(ARG_POSITION)
    }

    override fun onResume() {
        super.onResume()
        _viewModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields(view)
        initRecyclerView()
        initActions()
        initRefreshLayouts()
        observeViewModel()
    }

    fun setPositionChangeListener(listener: (Int) -> Unit) {
        _positionListener = listener
    }

    private fun refresh() {
        _viewModel.refresh()
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

        _adapter = NewsListItemAdapter( object: FeedItemEvent {
            override fun onItemDetails(item: News) {
                findNavController().navigate(R.id.action_watchFeedsFragment_to_watchDetailsFragment,
                    bundleOf(
                        NewsDetailsFragment.ARG_ITEM_ID to item.id
                    ))
            }
        })

        _binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val maxOffset = recyclerView.computeVerticalScrollRange()
                val offset = recyclerView.computeVerticalScrollOffset()
                _position = offset / (maxOffset / _adapter.items.size)
                _positionListener?.invoke(_position)
            }
        })
    }

    private fun observeViewModel() {
        _viewModel.state.observe(viewLifecycleOwner, ::render)
        collectEvent()
    }

    private fun collectEvent() {
        _viewModel.event
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleEvents(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEvents(evt: NewsListEvents) {
        when (evt) {
            is NewsListEvents.RefreshErrorEvent -> sendToast(evt.msgId)
            is NewsListEvents.RefreshSuccessEvent -> _binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun sendToast(textId: Int) {
        Toast.makeText(requireContext(), textId, Toast.LENGTH_LONG).show()
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
        _binding.recyclerView.scrollToPosition(_position)
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