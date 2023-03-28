package com.mukiva.rssreader.addrss.presentation

import com.mukiva.rssreader.watchfeeds.domain.Feed

data class AddRssState (
    val stateType: AddRssStateType,
    val errorMessage: Int?,
    val rssItem: Feed?
)
