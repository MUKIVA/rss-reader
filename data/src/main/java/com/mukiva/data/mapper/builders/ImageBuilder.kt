package com.mukiva.data.mapper.builders

import com.mukiva.data.entity.Image

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