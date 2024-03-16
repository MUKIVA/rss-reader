package com.mukiva.data

import com.mukiva.data.entity.Rss
import com.mukiva.data.gateway.IRssSearchGateway
import com.mukiva.data.mapper.RssParser
import com.mukiva.data.okhttp.BaseOkHttpSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import javax.inject.Inject
import okhttp3.Request

class RssSearchGatewayImpl @Inject constructor(
    private val client: OkHttpClient
) : BaseOkHttpSource(), IRssSearchGateway {

    private val mParser = RssParser()

    override suspend fun search(url: String): Rss {
        val request = Request.Builder()
            .url(url)
            .build()

        return withTimeout(TIMEOUT) {
            withContext(Dispatchers.IO) {
                val response = client.makeCall(request)
                mParser.parse(response.body!!.byteStream())
            }
        }
    }

    companion object {
        const val TIMEOUT: Long = 15L * 1000L
    }
}