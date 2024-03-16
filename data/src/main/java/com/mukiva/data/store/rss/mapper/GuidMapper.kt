package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Guid
import com.mukiva.data.store.rss.entity.GuidEntity
import javax.inject.Inject

class GuidMapper @Inject constructor() {

    fun mapAsEntity(guid: Guid) = GuidEntity(
        id = guid.id,
        text = guid.text,
        isPermaLink = guid.isPermaLink,
    )

    fun mapAsDomain(entity: GuidEntity) = Guid(
        id = entity.id,
        text = entity.text,
        isPermaLink = entity.isPermaLink,
    )

}