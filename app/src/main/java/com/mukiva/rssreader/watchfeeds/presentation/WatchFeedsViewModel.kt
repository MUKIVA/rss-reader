package com.mukiva.rssreader.watchfeeds.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mukiva.rssreader.R
import com.mukiva.rssreader.components.FeedInfoBottomSheetDialog
import com.mukiva.rssreader.watchfeeds.domain.RssItem

enum class FeedStateType {
    LOADING,
    EMPTY,
    NORMAL
}

data class FeedState(
    val stateType: FeedStateType,
    val feeds: MutableList<RssItem>,
)

class WatchFeedsViewModel : ViewModel() {

    private val _maxPageCount = 10
    private val _state = MutableLiveData<FeedState>()
    val state: MutableLiveData<FeedState> = _state

    init {
        _state.value = FeedState(
            stateType = FeedStateType.NORMAL,
            feeds = (1..10).map {
                RssItem(
                    rssTitle = "Title $it",
                    rssDescription = "Desc $it",
                    rssLink = "https:/google.com"
                ) }.toMutableList()
        )
    }

    fun deleteRssFeed(index: Int) {
        val copy = _state.value?.copy()
        copy?.feeds?.removeAt(index)
        _state.value = FeedState(
            stateType = if ((copy?.feeds?.size ?: 0) == 0)
                FeedStateType.EMPTY else FeedStateType.NORMAL,
            feeds = copy?.feeds ?: mutableListOf()
        )
    }

    fun addRssFeed(nav: NavController, ctx: Context) {
        if ((_state.value?.feeds?.size ?: 0) >= _maxPageCount) {
            Toast.makeText(ctx, R.string.max_feeds_count_msg, Toast.LENGTH_SHORT).show()
            return
        }
        nav.navigate(R.id.action_watchFeedsFragment_to_addRssFragment)
    }

    fun showDetailsRssFeed(index: Int, ctx: Context) {
        val dialog = BottomSheetDialog(ctx)
        val view = FeedInfoBottomSheetDialog(ctx)
        val rssItem = _state.value?.feeds?.get(index)
        view.setTitle(rssItem?.rssTitle ?: "Undefined")
        view.setDescription(rssItem?.rssDescription ?: "Undefined")
        dialog.setContentView(view)
        dialog.show()
    }

    fun refreshRssFeed(index: Int) {

    }
}