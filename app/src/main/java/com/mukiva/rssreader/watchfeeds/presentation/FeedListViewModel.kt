package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.parsing.entity.ChannelEntity
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.converters.RssConverter
import com.mukiva.rssreader.watchfeeds.data.RssService
import com.mukiva.rssreader.watchfeeds.domain.Feed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FeedListViewModel(
    private val _rssService: RssService,
) : SingleStateViewModel<FeedState>(
    FeedState(
        stateType = FeedStateType.LOADING,
        feeds = listOf()
    )
) {
    val eventFlow: SharedFlow<FeedEvents> by lazy { _eventFlow }
    private val _eventFlow = MutableSharedFlow<FeedEvents>()
    companion object {
        const val MAX_PAGE_COUNT = 10
        const val UNDEFINED_MSG = "Undefined"
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

    fun deleteFeed(index: Int) = viewModelScope.launch {
        val ent = _rssService.getAllRss()[index]
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssService.deleteRss(ent).map { createFeed(it) }
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    }

    fun loadFeeds() = viewModelScope.launch {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssService.getAllRss().map { createFeed(it) }
        modifyState(FeedState(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    }

    private fun getFeedStateType(feeds: List<Feed>): FeedStateType {
        return if (feeds.isEmpty()) FeedStateType.EMPTY else FeedStateType.NORMAL
    }

    private fun handleTriggerDeleteFeed(index: Int) = viewModelScope.launch {
        val item = getState().feeds[index]
        _eventFlow.emit(FeedEvents.DeleteRssEvent(item))
    }

    private fun handleTriggerAddFeed() = viewModelScope.launch {
        if (getState().feeds.size >= MAX_PAGE_COUNT)
            _eventFlow.emit(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            _eventFlow.emit(FeedEvents.AddRssEvent)
    }

    private fun handleTriggerAboutFeedDialog(index: Int) = viewModelScope.launch {
        _eventFlow.emit(FeedEvents.ShowFeedDetails(getState().feeds[index]))
    }

    private suspend fun createFeed(channelEntity: ChannelEntity): Feed {
           return Feed(
               title = channelEntity.title,
               description = channelEntity.description,
               newsRepoLink = channelEntity.link,
               imageLink = channelEntity.imageUrl,
               news = _rssService.getChannelItems(channelEntity).map {
                   RssConverter.itemEntityToNewsConverter.convert(it)
               }
           )
    }
}