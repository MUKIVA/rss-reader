package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.data.parsing.entity.ChannelEntity
import com.mukiva.rssreader.addrss.data.parsing.entity.ItemEntity
import com.mukiva.rssreader.watchfeeds.converters.RssConverter
import com.mukiva.rssreader.watchfeeds.domain.RssStorage
import io.objectbox.BoxStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ORMRssStorage(
    store: BoxStore
) : RssStorage {

    private val _channelBox = store.boxFor(ChannelEntity::class.java)
    private val _itemBox = store.boxFor(ItemEntity::class.java)
    private val _converter = RssConverter()

    override suspend fun update(rss: Rss, id: Long): Channel {
        return withContext(Dispatchers.IO) {
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
            _converter.entityToChannel(_channelBox[id])
        }
    }

    override suspend fun delete(id: Long): List<Channel> {
        return withContext(Dispatchers.IO) {
            _channelBox.remove(id)
            getAllRss()
        }
    }

    override suspend fun add(rss: Rss): List<Channel> {
        return withContext(Dispatchers.IO) {
            val entity = createChannel(rss)
            rss.channel.items.forEach { entity.items.add(createItemEntity(it)) }
            _channelBox.put(entity)
            getAllRss()
        }
    }

    override suspend fun getAllRss(): List<Channel> {
        return withContext(Dispatchers.IO) {
            _channelBox.all.map { _converter.entityToChannel(it) }
        }
    }

    override suspend fun getItems(id: Long): List<Item> {
        return withContext(Dispatchers.IO) {
            _channelBox[id].items.map { _converter.entityToItem(it) }
        }
    }

    override suspend fun getRss(id: Long): Channel {
        return withContext(Dispatchers.IO) {
            val entity = _channelBox[id]
            _converter.entityToChannel(entity)
        }
    }

    override suspend fun getItem(id: Long): Item {
        return withContext(Dispatchers.IO) {
            _converter.entityToItem(_itemBox[id])
        }
    }

    private fun createChannel(rss: Rss): ChannelEntity {
        return ChannelEntity(
            id = rss.channel.id,
            title = rss.channel.title,
            link = rss.channel.link,
            description = rss.channel.description,
            imageUrl = rss.channel.image?.url,
            refreshLink = rss.refreshLink
        )
    }

    private fun createItemEntity(item: Item): ItemEntity {
        return ItemEntity(
            id = item.id,
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