package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Channel
import com.mukiva.data.store.rss.entity.ChannelEntity
import javax.inject.Inject

class ChannelMapper @Inject constructor(
    private val itemsMapper: ItemsMapper,
    private val categoryMapper: CategoryMapper,
    private val cloudMapper: CloudMapper,
    private val imageMapper: ImageMapper,
    private val textInputMapper: TextInputMapper
) {

    fun mapAsEntity(channel: Channel) = ChannelEntity(
        id = channel.id,
        refreshLink = channel.refreshLink,
        title = channel.title,
        link = channel.link,
        description = channel.description,
        language = channel.language,
        copyright = channel.copyright,
        managingEditor = channel.managingEditor,
        webMaster = channel.webMaster,
        pubDate = channel.pubDate,
        lastBuildDate = channel.lastBuildDate,
        generator = channel.generator,
        docs = channel.docs,
        ttl = channel.ttl,
        rating = channel.rating,
        skipHours = channel.skipHours,
        skipDays = channel.skipDays,
    ).apply {
        channel.items.forEach {
            items.add(itemsMapper.mapAsEntity(it))
        }
        channel.category.forEach {
            category.add(categoryMapper.mapAsEntity(it))
        }
        cloud.target = channel.cloud?.let { cloudMapper.mapAsEntity(it) }
        image.target = channel.image?.let { imageMapper.mapAsEntity(it) }
        textInput.target = channel.textInput?.let { textInputMapper.mapAsEntity(it) }
    }

    fun mapAsDomain(entity: ChannelEntity) = Channel(
        id = entity.id,
        refreshLink = entity.refreshLink,
        title = entity.title,
        link = entity.link,
        description = entity.description,
        language = entity.language,
        copyright = entity.copyright,
        managingEditor = entity.managingEditor,
        webMaster = entity.webMaster,
        pubDate = entity.pubDate,
        lastBuildDate = entity.lastBuildDate,
        category = entity.category.map {
            categoryMapper.mapAsDomain(it)
        },
        generator = entity.generator,
        docs = entity.docs,
        cloud = entity.cloud.getOrNull()?.let { cloudMapper.mapAsDomain(it) },
        ttl = entity.ttl,
        image = entity.image.getOrNull()?.let { imageMapper.mapAsDomain(it) },
        rating = entity.rating,
        textInput = entity.textInput.getOrNull()?.let { textInputMapper.mapAsDomain(it) },
        skipHours = entity.skipHours,
        skipDays = entity.skipDays,
        items = entity.items.map {
            itemsMapper.mapAsDomain(it)
        },
    )
}