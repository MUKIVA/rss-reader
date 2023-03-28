package com.mukiva.rssreader.addrss.parsing.elements

data class Enclosure(
    val url: String,
    val length: Long,
    val type: String
)