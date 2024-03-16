package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Source
import com.mukiva.data.store.rss.entity.SourceEntity
import javax.inject.Inject

class SourceMapper @Inject constructor() {

    fun mapAsEntity(source: Source) = SourceEntity(
        id = source.id,
        url = source.url,
        text = source.text,
    )

    fun mapAsDomain(entity: SourceEntity) = Source(
        id = entity.id,
        url = entity.url,
        text = entity.text,
    )

}