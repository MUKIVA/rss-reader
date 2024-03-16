package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Rss
import com.mukiva.data.store.rss.entity.RssEntity
import io.objectbox.relation.ToOne
import javax.inject.Inject

fun <T> ToOne<T>.getOrNull() = when(isNull) {
    true -> null
    false -> target
}


class RssMapper @Inject constructor(
    private val channelMapper: ChannelMapper
) {

    fun mapAsEntity(rss: Rss) = RssEntity(
         id = rss.id,
         version = rss.version
    ).apply {
        channel.target = channelMapper.mapAsEntity(rss.channel)
    }

    fun mapAsDomain(entity: RssEntity) = Rss(
        id = entity.id,
        version = entity.version,
        channel = channelMapper.mapAsDomain(entity.channel.target)
    )

}

