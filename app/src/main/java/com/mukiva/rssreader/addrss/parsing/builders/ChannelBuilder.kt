package com.mukiva.rssreader.addrss.parsing.builders

import com.mukiva.rssreader.addrss.parsing.elements.*
import java.util.*

class ChannelBuilder {
    var title: String? = ""
    var link: String? = null
    var description: String? = ""
    var language: String? = null
    var copyright: String? = null
    var managingEditor: String? = null
    var webMaster: String? = null
    var pubDate: Date? = null
    var lastBuildDate: Date? = null
    var generator: String? = null
    var docs: String? = null
    var cloud: Cloud? = null
    var ttl: String? = null
    var image: Image? = null
    var rating: String? = null
    var textInput: TextInput? = null
    var skipHours: Int? = null
    var skipDays: Int? = null

    private val _items: MutableList<Item> = mutableListOf()
    private val _category: MutableList<Category> = mutableListOf()

    fun build(): Channel {
        return Channel(
            title ?: throw IllegalStateException(),
            link ?: throw IllegalStateException(),
            description ?: throw IllegalStateException(),
            language,
            copyright,
            managingEditor,
            webMaster,
            pubDate,
            lastBuildDate,
            _category,
            generator,
            docs,
            cloud,
            ttl,
            image,
            rating,
            textInput,
            skipHours,
            skipDays,
            _items
        )
    }

    fun addCategory(item: Category) {
        _category.add(item)
    }

    fun addItem(item: Item) {
        _items.add(item)
    }
}