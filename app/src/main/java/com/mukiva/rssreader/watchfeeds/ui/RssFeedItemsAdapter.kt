package com.mukiva.rssreader.watchfeeds.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.rssreader.databinding.ViewFeedListItemBinding
import com.mukiva.rssreader.watchfeeds.domain.FeedItem

interface FeedItemActions {
    fun onItemDetails(item: FeedItem)
}

class RssFeedItemsAdapter(
    private val _actionListener: FeedItemActions
) : RecyclerView.Adapter<RssFeedItemsAdapter.ItemsViewHolder>(), View.OnClickListener {

    class ItemsViewHolder(
        val binding: ViewFeedListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var items: List<FeedItem> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewFeedListItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ItemsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            holder.itemView.tag = item
            feedItemTitleText.text = item.title
            feedItemDescriptionText.text = item.description
            feedItemDateText.text = item.date.toString()
        }
    }

    override fun onClick(v: View) {
        val item = v.tag as FeedItem
        _actionListener.onItemDetails(item)
    }
}