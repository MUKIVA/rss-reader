package com.mukiva.rssreader.watchfeeds.ui

import androidx.recyclerview.widget.DiffUtil
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary

class FeedFragmentDiffUtilCallback(
    private val _oldList: List<FeedSummary>,
    private val _newList: List<FeedSummary>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = _oldList.size

    override fun getNewListSize(): Int = _newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = _oldList[oldItemPosition]
        val new = _newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = _oldList[oldItemPosition]
        val new = _newList[newItemPosition]
        return old == new
    }
}