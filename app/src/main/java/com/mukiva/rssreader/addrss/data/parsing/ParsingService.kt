package com.mukiva.rssreader.addrss.data.parsing

import java.io.InputStream

interface ParsingService<TParse> {
    fun parse(stream: InputStream): TParse
}