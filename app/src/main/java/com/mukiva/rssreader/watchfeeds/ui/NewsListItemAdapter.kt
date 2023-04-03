package com.mukiva.rssreader.watchfeeds.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mukiva.rssreader.R
import com.mukiva.rssreader.databinding.ViewNewsListItemBinding
import com.mukiva.rssreader.watchfeeds.domain.News
import java.text.SimpleDateFormat
import java.util.*

class NewsListItemAdapter(
    private val _actionListener: FeedItemEvent,
) : RecyclerView.Adapter<NewsListItemAdapter.ItemsViewHolder>(){

    class ItemsViewHolder(
        val binding: ViewNewsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    var items: List<News> = emptyList()
        set(value) {
            val diffCallback = NewsDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value.toList()
            diffResult.dispatchUpdatesTo(this)
        }

    companion object {
        @SuppressLint("ConstantLocale")
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
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

            feedItemTitleText.text = item.title
            feedItemDescriptionText.text = item.description.trim()
            feedItemDateText.text = item.date?.let { formatter.format(it) }

            Glide.with(holder.itemView.context)
                .load(item.imageLink)
                .placeholder(R.drawable.ic_no_image)
                .transform(CenterCrop(), RoundedCorners(14))
                .into(feedItemImage)

            holder.itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    private fun onClick(item: News) {
        _actionListener.onItemDetails(item)
    }
}