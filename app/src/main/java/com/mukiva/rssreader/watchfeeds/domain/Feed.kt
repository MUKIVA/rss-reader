package com.mukiva.rssreader.watchfeeds.domain

data class Feed(
    val id: Long,
    val title: String,
    val description: String,
    val newsRepoLink: String,
    val imageLink: String?,
    val news: List<News>
)