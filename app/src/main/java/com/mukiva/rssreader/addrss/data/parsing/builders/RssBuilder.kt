package com.mukiva.rssreader.addrss.data.parsing.builders

import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss

class RssBuilder {
    var version: String = "1.0"
    var channel: Channel? = null

    fun build(): Rss {
        return Rss(
            version = version,
            channel = channel ?: throw IllegalStateException()
        )
    }
}