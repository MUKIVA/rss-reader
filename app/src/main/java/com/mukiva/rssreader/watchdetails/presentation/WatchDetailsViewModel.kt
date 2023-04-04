package com.mukiva.rssreader.watchdetails.presentation

import android.os.Build
import android.text.Html
import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchfeeds.data.RssStorage
import java.text.SimpleDateFormat
import java.util.*

class WatchDetailsViewModel(
    private val _rssStore: RssStorage
) : SingleStateViewModel<WatchDetailsState, Nothing>(
    WatchDetailsState(
        stateType = WatchDetailsStateType.LOADING
    )
) {
    private val _formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    suspend fun setNews(id: Long) {
        modifyState(getState().copy(stateType = WatchDetailsStateType.LOADING))
        val news = _rssStore.getItem(id)

        val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(news.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(news.description)
        }

        modifyState(getState().copy(
            stateType = WatchDetailsStateType.NORMAL,
            title = news.title ?: "",
            description = content,
            imageLink = news.enclosure?.url ?: "",
            dateString = news.pubDate?.let { _formatter.format(it) } ?: "",
            originalLink = news.link ?: ""
        ))
    }

}