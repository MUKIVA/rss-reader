package com.mukiva.rssreader.addrss.data.parsing

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import java.io.InputStream

interface Parser {
    fun parse(stream: InputStream): Rss
}