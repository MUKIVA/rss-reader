package com.mukiva.rssreader.addrss.parsing.elements

data class Rss(
    val version: String = "1.0",
    val channel: Channel = Channel()
)