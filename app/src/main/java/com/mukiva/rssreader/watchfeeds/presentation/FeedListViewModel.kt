package com.mukiva.rssreader.watchfeeds.presentation

import com.mukiva.rssreader.R
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import com.mukiva.rssreader.watchfeeds.domain.FeedSummary

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

    suspend fun triggerDeleteFeed(index: Int) {
        val item = getState().feeds[index]
        event(FeedEvents.DeleteRssEvent(item))
    }

    suspend fun triggerAddFeed() {
        if (getState().feeds.size >= MAX_PAGE_COUNT)
            event(FeedEvents.ShowToastEvent(R.string.max_feeds_count_msg))
        else
            event(FeedEvents.AddRssEvent)
    }

    suspend fun triggerAboutFeedDialog(index: Int) {
        event(FeedEvents.ShowFeedDetails(getState().feeds[index]))
    }

    suspend fun deleteFeed(id: Long) {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssStorage.delete(id).map { createFeedSummary(it) }
        modifyState(getState().copy(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
    }

    suspend fun loadFeeds() {
        modifyState(getState().copy(stateType = FeedStateType.LOADING))
        val feeds = _rssStorage.getAllRss().map { createFeedSummary(it) }
        modifyState(getState().copy(
            stateType = getFeedStateType(feeds),
            feeds = feeds
        ))
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