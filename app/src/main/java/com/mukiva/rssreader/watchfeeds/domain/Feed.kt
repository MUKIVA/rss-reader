package com.mukiva.rssreader.watchfeeds.domain

data class Feed(
    val title: String,
    val description: String,
    val newsRepoLink: String,
    var news: MutableList<News>
)