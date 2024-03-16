package com.mukiva.data.mapper.builders

import com.mukiva.data.entity.Channel
import com.mukiva.data.entity.Rss

class RssBuilder {
    var version: String = "1.0"
    var channel: Channel? = null

    fun build(): Rss {
        return Rss(
            id = 0,
            version = version,
            channel = channel ?: error("Fail to parse RSS")
        )
    }
}