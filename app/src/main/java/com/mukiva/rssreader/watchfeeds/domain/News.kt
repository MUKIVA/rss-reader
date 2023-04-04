package com.mukiva.rssreader.watchfeeds.domain

import java.util.*

data class News(
    val id: Long,
    val title: String,
    val description: String,
    val date: Date?,
    val imageLink: String?,
    val originalLink: String
)