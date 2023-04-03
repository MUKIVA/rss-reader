package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.addrss.data.parsing.elements.*
import com.mukiva.rssreader.addrss.data.parsing.entity.ChannelEntity
import com.mukiva.rssreader.addrss.data.parsing.entity.ItemEntity
import com.mukiva.rssreader.watchfeeds.converters.RssConverter
import io.objectbox.BoxStore

class ORMRssStorage(
    store: BoxStore
) : RssStorage {

    private val _channelBox = store.boxFor(ChannelEntity::class.java)
    private val _converter = RssConverter()

    override suspend fun update(rss: Rss, id: Long): Channel {
        val entity = _channelBox[id]
        entity.items.clear()
        entity.items.applyChangesToDb()
        rss.channel.items.forEach { entity.items.add(createItemEntity(it)) }
        _channelBox.put(entity.copy(
            title = rss.channel.title,
            link = rss.channel.link,
            description = rss.channel.description,
            imageUrl = rss.channel.image?.url
        ))
        entity.items.applyChangesToDb()
        return _converter.entityToChannel(_channelBox[id])
    }

    override suspend fun delete(id: Long): List<Channel> {
        _channelBox.remove(id)
        return getAllRss()
    }

    override suspend fun add(rss: Rss): List<Channel> {
        val entity = createChannel(rss)
        rss.channel.items.forEach { entity.items.add(createItemEntity(it)) }
        _channelBox.put(entity)
        return getAllRss()
    }

    override suspend fun getAllRss(): List<Channel> {
        return _channelBox.all.map { _converter.entityToChannel(it) }
    }

    override suspend fun getItems(id: Long): List<Item> {
        return _channelBox[id].items.map { _converter.entityToItem(it) }
    }

    override suspend fun getRss(id: Long): Channel {
        val entity = _channelBox[id]
        return _converter.entityToChannel(entity)
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