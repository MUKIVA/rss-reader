package com.mukiva.rssreader.watchdetails.presentation

import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel

enum class WatchDetailsStateType {
    NORMAL,
    PARSE_ERROR
}

data class WatchDetailsState(
    val stateType: WatchDetailsStateType,
)

class WatchDetailsViewModel : SingleStateViewModel<WatchDetailsState>(
    WatchDetailsState(
        stateType = WatchDetailsStateType.NORMAL
    )
) {

}