package com.mukiva.rssreader.addrss.parsing.elements

data class Image(
    val url: String,
    val title: String,
    val link: String,
    val width: Int?,
    val height: Int?,
    val description: String?
    )