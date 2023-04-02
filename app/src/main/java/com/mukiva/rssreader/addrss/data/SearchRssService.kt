package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss

interface SearchRssService {
    suspend fun search(link: String): Rss
}