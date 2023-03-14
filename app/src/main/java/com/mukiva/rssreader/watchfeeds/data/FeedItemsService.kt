package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.watchfeeds.domain.FeedItem
import java.util.*

typealias NewsListener = (news: List<FeedItem>) -> Unit

class FeedItemsService {

    private val _listeners = mutableSetOf<NewsListener>()
    private var _items = mutableListOf<FeedItem>()

    init {
        refresh(false)
    }

    fun refresh(notify: Boolean = true) {
         _items = (1..100).map { FeedItem(
            title = "Title $it",
            description = "Description $it",
            date = Date()
        ) }.toMutableList()

        if (!notify) return

        notifyChanges()
    }

    fun addListener(listener: NewsListener) {
        _listeners.add(listener)
        listener.invoke(_items)
    }

    fun removeListener(listener: NewsListener) {
        _listeners.remove(listener)
    }

    private fun notifyChanges() {
        _listeners.forEach { it.invoke(_items) }
    }

}