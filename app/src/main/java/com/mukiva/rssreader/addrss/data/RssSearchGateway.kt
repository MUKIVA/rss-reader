package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss

interface RssSearchGateway {
    suspend fun search(link: String): Rss
}