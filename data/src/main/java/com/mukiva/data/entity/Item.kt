package com.mukiva.data.entity

import java.util.Date

data class Item(
    val id: Long = 0,
    val guid: Guid? = null,
    val title: String? = null,
    val description: String = "",
    val link: String? = null,
    val pubDate: Date? = null,
    val enclosure: Enclosure? = null,
    val author: String? = null,
    val category: List<Category> = listOf(),
    val comments: String? = null,
    val source: Source? = null
)