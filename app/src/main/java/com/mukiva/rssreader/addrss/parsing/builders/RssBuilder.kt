package com.mukiva.rssreader.addrss.parsing.builders

import com.mukiva.rssreader.addrss.parsing.elements.Channel
import com.mukiva.rssreader.addrss.parsing.elements.Rss

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