package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Enclosure
import com.mukiva.data.store.rss.entity.EnclosureEntity
import javax.inject.Inject

class EnclosureMapper @Inject constructor() {

    fun mapAsEntity(enclosure: Enclosure) = EnclosureEntity(
        id = enclosure.id,
        url = enclosure.url,
        length = enclosure.length,
        type = enclosure.type,
    )

    fun mapAsDomain(entity: EnclosureEntity) = Enclosure(
        id = entity.id,
        url = entity.url,
        length = entity.length,
        type = entity.type,
    )

}