package com.mukiva.rssreader.addrss.parsing.builders

import com.mukiva.rssreader.addrss.parsing.elements.*
import java.util.Date

class ItemBuilder {
    var guid: Guid? = null
    var title: String? = ""
    var description: String? = ""
    var link: String? = null
    var pubDate: Date? = null
    var enclosure: Enclosure? = null
    var author: String? = null
    var comments: String? = null
    var source: Source? = null

    private val _category: MutableList<Category> = mutableListOf()

    fun build(): Item {

        if (title.isNullOrEmpty() && description.isNullOrEmpty())
            throw IllegalStateException()

        return Item(
            guid,
            title,
            description,
            link,
            pubDate,
            enclosure,
            author,
            _category,
            comments,
            source
        )
    }

    fun addCategory(item: Category) {
        _category.add(item)
    }
}