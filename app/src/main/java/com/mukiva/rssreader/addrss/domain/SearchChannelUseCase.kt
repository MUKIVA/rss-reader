package com.mukiva.rssreader.addrss.domain

import com.mukiva.rssreader.addrss.data.RssSearchGateway
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import kotlinx.coroutines.TimeoutCancellationException
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okio.IOException

class SearchChannelUseCase(
    private val _searchGateway: RssSearchGateway
) {

    suspend operator fun invoke(link: String): Result<Rss> {
        var url = link.lowercase()
        if (!link.matches(Regex("^(https://).*$")))
            url = "https://$link"

        if (!isValidUrl(url)) return Error(InvalidUrlError)

        return try {
            val newRss = _searchGateway.search(url)
            Success(newRss)
        } catch (e: Exception) {
            when (e) {
                is TimeoutCancellationException -> Error(TimeoutError)
                is IOException -> Error(ConnectionError)
                else -> Error(UnknownError)
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return url.toHttpUrlOrNull() != null
    }
}
