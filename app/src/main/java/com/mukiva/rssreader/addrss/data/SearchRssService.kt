package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.watchfeeds.domain.Feed

interface SearchRssService {
    suspend fun search(link: String): Feed
}