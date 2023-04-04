package com.mukiva.rssreader.addrss.data.parsing

import java.io.InputStream

interface Parser<TParse> {
    fun parse(stream: InputStream): TParse
}