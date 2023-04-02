package com.mukiva.rssreader.watchdetails.presentation

import com.mukiva.rssreader.core.viewmodel.SingleStateViewModel

class WatchDetailsViewModel : SingleStateViewModel<WatchDetailsState, Nothing>(
    WatchDetailsState(
        stateType = WatchDetailsStateType.NORMAL
    )
) {

}