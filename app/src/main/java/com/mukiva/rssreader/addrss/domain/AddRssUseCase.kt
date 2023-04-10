package com.mukiva.rssreader.addrss.domain

import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.watchfeeds.domain.RssStorage

class AddRssUseCase(
    private val _rssStorage: RssStorage,
) {
    suspend operator fun invoke(rss: Rss): Result<List<Channel>> {
        return try {
            val list = _rssStorage.add(rss)
            Success(list)
        } catch (e: Exception) {
            Error(UnknownError)
        }
    }
}