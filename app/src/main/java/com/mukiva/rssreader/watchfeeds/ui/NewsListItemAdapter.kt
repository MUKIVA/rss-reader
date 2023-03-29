package com.mukiva.rssreader.watchfeeds.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mukiva.rssreader.databinding.ViewNewsListItemBinding
import com.mukiva.rssreader.watchfeeds.domain.News

class NewsListItemAdapter(
    private val _actionListener: FeedItemEvent
) : RecyclerView.Adapter<NewsListItemAdapter.ItemsViewHolder>(), View.OnClickListener {

    class ItemsViewHolder(
        val binding: ViewNewsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var items: List<News> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            val diffCallback = NewsDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewNewsListItemBinding.inflate(inflater, parent, false)

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

            val drawable = feedItemImage.drawable

            Glide.with(holder.itemView)
                .load(item.imageLink)
                .transform(CenterCrop(), RoundedCorners(14))
                .into(feedItemImage)
                .onLoadFailed(drawable)

            root.setOnClickListener(this@NewsListItemAdapter)
        }
    }

    override fun onClick(v: View) {
        val item = v.tag as News
        _actionListener.onItemDetails(item)
    }
}