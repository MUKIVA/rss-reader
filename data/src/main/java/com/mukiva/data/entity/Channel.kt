package com.mukiva.data.entity

import java.util.Collections.emptyList
import java.util.Date

data class Channel(
    val id: Long = 0,
    val refreshLink: String = "",
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val language: String? = null,
    val copyright: String? = null,
    val managingEditor: String? = null,
    val webMaster: String? = null,
    val pubDate: Date? = null,
    val lastBuildDate: Date? = null,
    val category: List<Category> = emptyList(),
    val generator: String? = null,
    val docs: String? = null,
    val cloud: Cloud? = null,
    val ttl: String? = null,
    val image: Image? = null,
    val rating: String? = null,
    val textInput: TextInput? = null,
    val skipHours: Int? = null,
    val skipDays: Int? = null,
    val items: List<Item> = emptyList()
)