package com.mukiva.rssreader.watchfeeds.ui

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary
import kotlinx.coroutines.FlowPreview

@FlowPreview
class NewsListFragmentAdapter(
    parent: Fragment,
    initializeState: List<FeedSummary> = listOf()
) : FragmentStateAdapter(parent) {

    var feedSummaries: List<FeedSummary> = initializeState
        set (value) {
            val diffCallback = FeedFragmentDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun getItemCount(): Int = feedSummaries.size

    override fun getItemId(position: Int): Long {
        return feedSummaries[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        return feedSummaries.firstOrNull { it.id == itemId } != null
    }

    override fun createFragment(position: Int): Fragment {
        val feed = feedSummaries[position]
        return NewsListFragment(feed.id)
    }
}