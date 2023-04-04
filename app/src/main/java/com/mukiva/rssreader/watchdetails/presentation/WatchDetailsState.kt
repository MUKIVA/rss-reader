package com.mukiva.rssreader.watchdetails.presentation

data class WatchDetailsState(
    val stateType: WatchDetailsStateType,
    val title: String = "",
    val description: String = "",
    val imageLink: String = "",
    val originalLink: String = "",
    val dateString: String = ""
)
