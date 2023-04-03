package com.mukiva.rssreader.watchfeeds.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mukiva.rssreader.watchfeeds.domain.Feed

class NewsListFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var fragments: List<Feed> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        val feed = fragments[position]
        return NewsListFragment(feed.id)
    }
}