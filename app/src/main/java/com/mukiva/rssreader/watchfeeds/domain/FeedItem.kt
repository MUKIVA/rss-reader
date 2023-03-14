package com.mukiva.rssreader.watchfeeds.domain

import java.util.*

data class FeedItem(
    val title: String,
    val description: String,
    val date: Date
)