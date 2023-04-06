package com.mukiva.rssreader.watchfeeds.domain

import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.domain.*
import com.mukiva.rssreader.addrss.domain.UnknownError
import kotlinx.coroutines.TimeoutCancellationException
import okio.IOException

class RefreshChannelUseCase(
    private val _rssStorage: RssStorage,
    private val _rssSearchService: RssSearchGateway
) {

    suspend operator fun invoke(id: Long): Result<Channel> {
        return try {
            val channel = _rssStorage.getRss(id)
            val rss = _rssSearchService.search(channel.refreshLink)
            Success(_rssStorage.update(rss, id))
        } catch (e: Exception) {
            when (e) {
                is TimeoutCancellationException -> Error(TimeoutError)
                is IOException -> Error(ConnectionError)
                else -> Error(UnknownError)
            }
        }
    }

}