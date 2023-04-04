package com.mukiva.rssreader.watchfeeds.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary
import kotlinx.coroutines.FlowPreview

@FlowPreview
class NewsListFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    var feedSummaries: List<FeedSummary> = emptyList()
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