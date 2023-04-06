package com.mukiva.rssreader.watchfeeds.presentation

import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.utils.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary
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

    fun setPage(page: Int) {
        modifyState(getState().copy(
            currentPage = page
        ))
    }

    fun setScrollMap(map: Map<Long, Int>) {
        modifyState(getState().copy(
            scrollMap = map
        ))
    }

    fun triggerDeleteFeed(index: Int) {
        viewModelScope.launch {
            val item = getState().feeds[index]
            event(FeedEvents.DeleteRssEvent(item))
        }
    }

    fun triggerAddFeed() {
        viewModelScope.launch {
            if (getState().feeds.size >= MAX_PAGE_COUNT)
                event(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
            else
                event(FeedEvents.AddRssEvent)
        }
    }

    fun triggerAboutFeedDialog(index: Int) {
        viewModelScope.launch {
            event(FeedEvents.ShowFeedDetailsEvent(getState().feeds[index]))
        }
    }

    fun deleteFeed(id: Long) {
        viewModelScope.launch {
            modifyState(getState().copy(stateType = FeedStateType.LOADING))
            val feeds = _rssStorage.delete(id).map { createFeedSummary(it) }
            modifyState(getState().copy(
                stateType = getFeedStateType(feeds),
                feeds = feeds
            ))
        }
    }

    fun loadFeeds() {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        viewModelScope.launch {
            val feeds = _rssStorage.getAllRss().map { createFeedSummary(it) }
            val oldSize = getState().feeds.size
            modifyState(getState().copy(
                stateType = getFeedStateType(feeds),
                feeds = feeds
            ))

            if (feeds.size > oldSize)
                event(FeedEvents.NewRssAdded(oldSize))
        }
    }

    private fun getFeedStateType(feeds: List<FeedSummary>): FeedStateType {
        return if (feeds.isEmpty()) FeedStateType.EMPTY else FeedStateType.NORMAL
    }

    private fun createFeedSummary(channelEntity: Channel): FeedSummary {
           return FeedSummary(
               id = channelEntity.id,
               title = channelEntity.title,
               description = channelEntity.description,
               newsRepoLink = channelEntity.link,
               imageLink = channelEntity.image?.link
           )
    }
}