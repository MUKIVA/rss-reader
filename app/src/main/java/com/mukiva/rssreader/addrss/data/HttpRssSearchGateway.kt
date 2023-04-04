package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.addrss.data.parsing.RssParser
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.okhttp.AsyncCallCallbacks
import com.mukiva.rssreader.okhttp.BaseOkHttpSource
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HttpRssSearchGateway : BaseOkHttpSource(
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
), RssSearchGateway {

    private val _rssParser = RssParser()

    companion object {
        const val TIMEOUT: Long = 15000
    }

    override suspend fun search(link: String): Rss {

        val request = Request.Builder()
            .url(link)
            .build()

        return withTimeout(TIMEOUT) {
            withContext(Dispatchers.IO) {
                val response = client.newCall(request).suspendEnqueue()

                if (!response.isSuccessful)
                    throw Exception("Unknown error")

                _rssParser.parse(response.body!!.byteStream()).copy(refreshLink = link)
            }
        }
    }
}