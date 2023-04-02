package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.data.parsing.RssParsingService
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.domain.*
import com.mukiva.rssreader.okhttp.AsyncCallCallbacks
import com.mukiva.rssreader.okhttp.BaseOkHttpSource
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HttpSearchRssService : BaseOkHttpSource(
    object : AsyncCallCallbacks {
        override fun onCancel(continuation: CancellableContinuation<Response>) {
            continuation.cancel()
        }

        override fun onFail(
            call: Call,
            e: IOException,
            continuation: CancellableContinuation<Response>
        ) {
            continuation.resumeWithException(e)
        }

        override fun onSuccess(
            call: Call,
            response: Response,
            continuation: CancellableContinuation<Response>
        ) {
            continuation.resume(response)
        }
    }
), SearchRssService {

    private val _rssParser = RssParsingService()

    companion object {
        const val TIMEOUT: Long = 15000
    }

    override suspend fun search(link: String): Result<Rss> {

        if (!validateUrl(link)) return Error(InvalidUrlError)

        val request = Request.Builder()
            .url(link)
            .build()

        try {

            return withTimeout(TIMEOUT) {

                val response = client.newCall(request).suspendEnqueue()

                if (!response.isSuccessful) return@withTimeout Error(UnknownError)

                return@withTimeout withContext(Dispatchers.IO) {
                    val rss: Rss = _rssParser.parse(response.body!!.byteStream())
                    Success<Rss>(rss)
                }
            }

        } catch (e: Exception) {
            return when (e) {
                is TimeoutCancellationException -> Error(TimeoutError)
                is IOException -> Error(ConnectionError)
                else -> Error(UnknownError)
            }
        }

    }

    private fun validateUrl(url: String): Boolean {
        return url.toHttpUrlOrNull() != null
    }
}