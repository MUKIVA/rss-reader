package com.mukiva.rssreader.watchfeeds.converters
import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Enclosure
import com.mukiva.rssreader.addrss.data.parsing.elements.Image
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.data.parsing.entity.ChannelEntity
import com.mukiva.rssreader.addrss.data.parsing.entity.ItemEntity

class RssConverter {

    fun entityToChannel(obj: ChannelEntity): Channel {
        return Channel(
            id = obj.id,
            title = obj.title,
            description = obj.description,
            link = obj.link,
            image = obj.imageUrl?.let { Image(
                url = it, title = "", link = "",
                null, null, null
            ) },
            items = obj.items.map { entityToItem(it) },
            refreshLink = obj.refreshLink
        )

    }

    fun entityToItem(obj: ItemEntity): Item {

        val enclosure = if (
            obj.enclosureUrl.isNullOrEmpty()
            || obj.enclosureLength == null
            || obj.enclosureType.isNullOrEmpty()
        ) null else Enclosure(obj.enclosureUrl, obj.enclosureLength, obj.enclosureType)

        return Item(
            id = obj.id,
            title = obj.title,
            description = obj.description,
            link = obj.link,
            pubDate = obj.pubDate,
            enclosure = enclosure
        )
    }
}