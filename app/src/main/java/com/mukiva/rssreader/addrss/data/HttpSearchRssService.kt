package com.mukiva.rssreader.addrss.data

import androidx.media3.common.MimeTypes
import com.mukiva.rssreader.addrss.domain.SearchException
import com.mukiva.rssreader.addrss.parsing.RssParsingService
import com.mukiva.rssreader.addrss.parsing.elements.Rss
import com.mukiva.rssreader.okhttp.BaseOkHttpSource
import com.mukiva.rssreader.watchfeeds.domain.Feed
import com.mukiva.rssreader.watchfeeds.domain.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request
import java.util.*

class HttpSearchRssService : BaseOkHttpSource(), SearchRssService {

    companion object {
        const val TIMEOUT: Long = 15000
    }

    override suspend fun search(link: String): Feed {
        val url = validateUrl(link)
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val rss = getAndParseRss(request)
            return createFeed(rss)
        } catch (e: Exception)  {
            throw SearchException.InvalidUrlException()
        }
    }

    private fun validateUrl(url: String): HttpUrl {
        return url.toHttpUrlOrNull() ?: throw SearchException.InvalidUrlException()
    }

    private fun createFeed(rss: Rss): Feed {
        return Feed(
            title = rss.channel.title,
            description = rss.channel.description,
            newsRepoLink = rss.channel.link,
            imageLink = rss.channel.link,
            news = rss.channel.Items.map { item ->

                var imageLink = ""
                if (item.enclosure != null && item.enclosure.type == MimeTypes.IMAGE_JPEG)
                    imageLink = item.enclosure.url

                News(
                    title = item.title ?: "",
                    description = item.description ?: "",
                    date = item.pubDate ?: Date(),
                    imageLink = imageLink
                )

            }.toList())
    }

    private suspend fun getAndParseRss(request: Request): Rss = withTimeout(TIMEOUT) {
        return@withTimeout withContext(Dispatchers.IO) {
            val response = client.newCall(request).suspendEnqueue()
            RssParsingService().parse(response.body!!.byteStream())
        }
    }

}