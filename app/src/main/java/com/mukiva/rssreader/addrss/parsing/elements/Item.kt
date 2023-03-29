package com.mukiva.rssreader.addrss.parsing.elements

import java.util.*

data class Item(
    val guid: Guid? = null,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    val pubDate: Date? = null,
    val enclosure: Enclosure? = null,
    val author: String? = null,
    val category: List<Category> = listOf(),
    val comments: String? = null,
    val source: Source? = null
)