package com.mukiva.rssreader.glue.feed.mapper

import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Rss
import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssItem
import java.util.Date

object RssMapper {

    fun mapItem(item: Item) = object : IArticle {
        override val id: Long
            get() = item.id
        override val title: String
            get() = item.title ?: ""
        override val body: String
            get() = item.description
        override val date: Date
            get() = item.pubDate ?: Date()
        override val imgUrl: String
            get() = item.enclosure?.url ?: ""

    }

    fun map(rss: Rss, url: String) = object : IRss {
        override val name: String
            get() = rss.channel.title
        override val url: String
            get() = rss.channel.link
        override val originalUrl: String
            get() = url
        override val description: String
            get() = rss.channel.description
        override val imageUrl: String
            get() = rss.channel.image?.url ?: ""
        override val items: Collection<IRssItem>
            get() = rss.channel.items.map { item ->
                    object : IRssItem {
                        override val id: Long
                            get() = item.id
                        override val name: String
                            get() = item.title ?: ""
                        override val content: String
                            get() = item.description
                        override val pubDate: Date
                            get() = item.pubDate ?: Date()
                        override val imgUrl: String
                            get() = item.enclosure?.url ?: ""
                        override val imageType: String
                            get() = item.enclosure?.type ?: ""
                    }
            }
    }
}