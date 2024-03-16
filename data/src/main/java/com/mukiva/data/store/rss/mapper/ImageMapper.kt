package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Image
import com.mukiva.data.store.rss.entity.ImageEntity
import javax.inject.Inject

class ImageMapper @Inject constructor() {

    fun mapAsEntity(image: Image) = ImageEntity(
        id = image.id,
        url = image.url,
        title = image.title,
        link = image.link,
        width = image.width,
        height = image.height,
        description = image.description,
    )

    fun mapAsDomain(entity: ImageEntity) = Image(
        id = entity.id,
        url = entity.url,
        title = entity.title,
        link = entity.link,
        width = entity.width,
        height = entity.height,
        description = entity.description,
    )

}