package com.mukiva.rssreader.addrss.parsing.elements

import java.util.Date

data class Channel(
    val title: String,
    val link: String,
    val description: String,
    val language: String?,
    val copyright: String?,
    val managingEditor: String?,
    val webMaster: String?,
    val pubDate: Date?,
    val lastBuildDate: Date?,
    val category: List<Category>,
    val generator: String?,
    val docs: String?,
    val cloud: Cloud?,
    val ttl: String?,
    val image: Image?,
    val rating: String?,
    val textInput: TextInput?,
    val skipHours: Int?,
    val skipDays: Int?,
    val Items: List<Item>
    )