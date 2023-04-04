package com.mukiva.rssreader.watchdetails.presentation

import android.text.Spanned

data class WatchDetailsState(
    val stateType: WatchDetailsStateType,
    val title: String = "",
    val description: Spanned? = null,
    val imageLink: String = "",
    val originalLink: String = "",
    val dateString: String = ""
)
