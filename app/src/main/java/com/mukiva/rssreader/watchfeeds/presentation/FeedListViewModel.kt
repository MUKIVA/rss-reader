package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MimeTypes
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.launch

class FeedListViewModel(
    private val _rssStorage: RssStorage,
) : SingleStateViewModel<FeedState, FeedEvents>(
    FeedState(
        stateType = FeedStateType.LOADING,
        feeds = listOf()
    )
) {
    companion object {
        const val MAX_PAGE_COUNT = 10
    }

    init {
        loadFeeds()
    }

    fun triggerDeleteFeed(index: Int) {
        handleTriggerDeleteFeed(index)
    }

    fun triggerAddFeed() {
        handleTriggerAddFeed()
    }

    fun triggerAboutFeedDialog(index: Int) {
        handleTriggerAboutFeedDialog(index)
    }

    fun deleteFeed(id: Long) { viewModelScope.launch {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssStorage.delete(id).map { createFeed(it) }
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    } }

    fun loadFeeds() { viewModelScope.launch {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssStorage.getAllRss().map { createFeed(it) }
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    } }

    private fun getFeedStateType(feeds: List<Feed>): FeedStateType {
        return if (feeds.isEmpty()) FeedStateType.EMPTY else FeedStateType.NORMAL
    }

    private fun handleTriggerDeleteFeed(index: Int) = viewModelScope.launch {
        val item = getState().feeds[index]
        event(FeedEvents.DeleteRssEvent(item))
    }

    private fun handleTriggerAddFeed() = viewModelScope.launch {
        if (getState().feeds.size >= MAX_PAGE_COUNT)
            event(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            event(FeedEvents.AddRssEvent)
    }

    private fun handleTriggerAboutFeedDialog(index: Int) = viewModelScope.launch {
        event(FeedEvents.ShowFeedDetails(getState().feeds[index]))
    }

    private suspend fun createFeed(channelEntity: Channel): Feed {
           return Feed(
               id = channelEntity.id,
               title = channelEntity.title,
               description = channelEntity.description,
               newsRepoLink = channelEntity.link,
               imageLink = channelEntity.image?.link,
               news = _rssStorage.getItems(channelEntity.id).map {
                   itemToNews(it)
               }
           )
    }

    private fun itemToNews(item: Item): News {

        var imageLink: String? = null
        if (item.enclosure?.type != null && item.enclosure.type == MimeTypes.IMAGE_JPEG)
            imageLink = item.enclosure.url

        return News(
            title = item.title ?: "Undefined",
            description = item.description ?: "Undefined",
            date = item.pubDate,
            imageLink = imageLink,
            originalLink = item.link ?: ""
        )
    }
}