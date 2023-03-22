package com.mukiva.rssreader.addrss.data

import com.mukiva.rssreader.watchfeeds.domain.Feed
import okhttp3.*
import java.io.IOException


class HttpSearchRssService : SearchRssService {

    private val _client = OkHttpClient()

    override suspend fun search(link: String): Feed {

        val request = Request.Builder()
            .url("https://lenta.ru/rss")
            .build()

        _client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
        return Feed(
            title = "",
            description = "",
            newsRepoLink = "",
            news = mutableListOf()
        )
    }
}