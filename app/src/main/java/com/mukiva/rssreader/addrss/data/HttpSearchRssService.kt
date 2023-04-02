package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.domain.SearchException
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.okhttp.AsyncCallCallbacks
import com.mukiva.rssreader.okhttp.BaseOkHttpSource
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HttpSearchRssService : BaseOkHttpSource(
    object : AsyncCallCallbacks {
        override fun onCancel() {
            throw SearchException.TimeOutException()
        }

        override fun onFail(
            call: Call,
            e: IOException,
            continuation: CancellableContinuation<Response>
        ) {
            continuation.resumeWithException(SearchException.ConnectionException(e))
        }

        override fun onSuccess(
            call: Call,
            response: Response,
            continuation: CancellableContinuation<Response>
        ) {
            if (response.isSuccessful) {
                continuation.resume(response)
            } else {
                continuation.resumeWithException(SearchException.BackendException(response.code))
            }
        }
    }
), SearchRssService {

    companion object {
        const val TIMEOUT: Long = 15000
    }

    override suspend fun search(link: String): Rss {
        val url = validateUrl(link)
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            return getAndParseRss(request).copy(refreshLink = link)
        } catch (e: Exception)  {
            throw SearchException.InvalidUrlException()
        }
    }

    private fun validateUrl(url: String): HttpUrl {
        return url.toHttpUrlOrNull() ?: throw SearchException.InvalidUrlException()
    }

    private suspend fun getAndParseRss(request: Request): Rss = withTimeout(TIMEOUT) {
        return@withTimeout withContext(Dispatchers.IO) {
            val response = client.newCall(request).suspendEnqueue()
            com.mukiva.rssreader.addrss.data.parsing.RssParsingService().parse(response.body!!.byteStream())
        }
    }

}