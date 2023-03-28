package com.mukiva.rssreader.addrss.parsing.elements

import java.util.*

data class Item(
    val guid: Guid?,
    val title: String?,
    val description: String?,
    val link: String?,
    val pubDate: Date?,
    val enclosure: Enclosure?,
    val author: String?,
    val category: List<Category>,
    val comments: String?,
    val source: Source?
)