package com.mukiva.rssreader.addrss.data.parsing.elements

data class Rss(
    val version: String = "1.0",
    val channel: Channel = Channel(0, ""),
    val refreshLink: String = ""
)