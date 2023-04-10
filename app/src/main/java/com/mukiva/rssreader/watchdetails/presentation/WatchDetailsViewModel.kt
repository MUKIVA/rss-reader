package com.mukiva.rssreader.watchdetails.presentation

import android.os.Build
import android.text.Html
import androidx.lifecycle.viewModelScope
import com.mukiva.rssreader.addrss.domain.Error
import com.mukiva.rssreader.addrss.domain.Success
import com.mukiva.rssreader.utils.viewmodel.SingleStateViewModel
import com.mukiva.rssreader.watchdetails.domain.GetItemUseCase
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import kotlinx.coroutines.launch
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

    fun setNews(id: Long) {
        modifyState(getState().copy(stateType = WatchDetailsStateType.LOADING))
        viewModelScope.launch {

            when (val result = GetItemUseCase(_rssStore).invoke(id)) {
                is Error -> modifyState(getState().copy(
                    stateType = WatchDetailsStateType.LOADING
                ))
                is Success -> {
                    val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(result.data.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(result.data.description)
                    }

                    modifyState(getState().copy(
                        stateType = WatchDetailsStateType.NORMAL,
                        title = result.data.title ?: "",
                        description = content,
                        imageLink = result.data.enclosure?.url ?: "",
                        dateString = result.data.pubDate?.let { _formatter.format(it) } ?: "",
                        originalLink = result.data.link ?: ""
                    ))
                }
            }
        }
    }
}