package com.mukiva.rssreader.watchfeeds.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.rssreader.databinding.ViewNewsListItemBinding
import com.mukiva.rssreader.watchfeeds.domain.News

interface FeedItemActions {
    fun onItemDetails(item: News)
}

class NewsDiffUtilCallback(
    private val _oldList: List<News>,
    private val _newList: List<News>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = _oldList.size
    override fun getNewListSize(): Int = _newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = _oldList[oldItemPosition]
        val new = _newList[newItemPosition]
        return old.title == new.title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = _oldList[oldItemPosition]
        val new = _newList[newItemPosition]
        return old == new
    }
}

class NewsListItemAdapter(
    private val _actionListener: FeedItemActions
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
            root.setOnClickListener(this@NewsListItemAdapter)
        }
    }

    override fun onClick(v: View) {
        val item = v.tag as News
        _actionListener.onItemDetails(item)
    }
}