package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.domain.Result

interface SearchRssService {
    suspend fun search(link: String): Result<Rss>
}