package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.Enclosure
import com.mukiva.data.entity.Guid
import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Source
import com.mukiva.data.store.rss.entity.ItemEntity
import javax.inject.Inject

class ItemsMapper @Inject constructor(
    private val guidMapper: GuidMapper,
    private val enclosureMapper: EnclosureMapper,
    private val categoryMapper: CategoryMapper,
    private val sourceMapper: SourceMapper
) {

    fun mapAsEntity(item: Item) = ItemEntity(
        id = item.id,
        title = item.title,
        description = item.description,
        link = item.link,
        pubDate = item.pubDate,
        author = item.author,
        comments = item.comments,
    ).apply {
        guid.target = guidMapper.mapAsEntity(item.guid ?: Guid())
        enclosure.target = enclosureMapper.mapAsEntity(item.enclosure ?: Enclosure())
        item.category.forEach {
            category.add(categoryMapper.mapAsEntity(it))
        }
        source.target = sourceMapper.mapAsEntity(item.source ?: Source())
    }

    fun mapAsDomain(entity: ItemEntity) = Item(
        id = entity.id,
        guid = entity.guid.getOrNull()?.let { guidMapper.mapAsDomain(it) },
        title = entity.title,
        description = entity.description ?: "",
        link = entity.link,
        pubDate = entity.pubDate,
        enclosure = entity.enclosure.getOrNull()?.let { enclosureMapper.mapAsDomain(it) },
        author = entity.author,
        category = entity.category.map {
            categoryMapper.mapAsDomain(it)
        },
        comments = entity.comments,
        source = entity.source.getOrNull()?.let { sourceMapper.mapAsDomain(it) },
    )

}