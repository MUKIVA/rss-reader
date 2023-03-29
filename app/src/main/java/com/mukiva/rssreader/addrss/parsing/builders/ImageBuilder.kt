package com.mukiva.rssreader.addrss.parsing.builders

import com.mukiva.rssreader.addrss.parsing.elements.Image

class ImageBuilder {
    var url: String? = null
    var title: String? = null
    var link: String? = null
    var width: Int? = null
    var height: Int? = null
    var description: String? = null

    fun build(): Image {
        return Image(
            url = url ?: throw IllegalStateException(),
            title = title ?: throw IllegalStateException(),
            link = link ?: throw IllegalStateException(),
            width = width,
            height = height,
            description = description
        )
    }
}