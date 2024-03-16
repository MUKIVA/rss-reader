package com.mukiva.data.mapper

import com.mukiva.data.entity.Rss
import java.io.InputStream

interface Parser {
    fun parse(stream: InputStream): Rss
}