package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.addrss.parsing.entity.*
import com.mukiva.rssreader.addrss.parsing.elements.*
import io.objectbox.BoxStore

class ORMRssService(
    store: BoxStore
) : RssService {

    private val _channelBox = store.boxFor(ChannelEntity::class.java)

    override suspend fun updateRss(rss: Rss, id: Long): ChannelEntity {
        val entity = _channelBox[id]
        entity.items.clear()
        rss.channel.items.forEach { entity.items.add(createItemEntity(it)) }
        _channelBox.put(entity.copy(
            title = rss.channel.title,
            link = rss.channel.link,
            description = rss.channel.description,
            imageUrl = rss.channel.image?.url
        ))
        return _channelBox[id]
    }

    override suspend fun deleteRss(rss: ChannelEntity): List<ChannelEntity> {
        _channelBox.remove(rss.id)
        return getAllRss()
    }

    override suspend fun addRss(rss: Rss): List<ChannelEntity> {
        val entity = createChannel(rss)
        rss.channel.items.forEach { entity.items.add(createItemEntity(it)) }
        _channelBox.put(entity)
        return getAllRss()
    }

    override suspend fun getAllRss(): List<ChannelEntity> {
        return _channelBox.all
    }

    override suspend fun getChannelItems(rss: ChannelEntity): List<ItemEntity> {
        return _channelBox[rss.id].items.toList()
    }

    private fun createChannel(rss: Rss): ChannelEntity {
        return ChannelEntity(
            title = rss.channel.title,
            link = rss.channel.link,
            description = rss.channel.description,
            imageUrl = rss.channel.image?.url,
            refreshLink = rss.refreshLink
        )
    }

    private fun createItemEntity(item: Item): ItemEntity {
        return ItemEntity(
            title = item.title,
            description = item.description,
            link = item.link,
            pubDate = item.pubDate,
            enclosureUrl = item.enclosure?.url,
            enclosureLength = item.enclosure?.length,
            enclosureType = item.enclosure?.type
        )
    }
}