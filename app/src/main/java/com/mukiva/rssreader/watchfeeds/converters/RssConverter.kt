package com.mukiva.rssreader.watchfeeds.converters

import androidx.media3.common.MimeTypes
import com.mukiva.rssreader.addrss.parsing.entity.ItemEntity
import com.mukiva.rssreader.watchfeeds.domain.News
import com.mukiva.rssreader.watchfeeds.presentation.FeedListViewModel

class RssConverter {

    companion object {

        val itemEntityToNewsConverter = object : TypeConverter<ItemEntity, News> {
            override fun convert(obj: ItemEntity): News {
                var imageLink: String? = null
                if (obj.enclosureType != null
                    && obj.enclosureUrl != null
                    && obj.enclosureType == MimeTypes.IMAGE_JPEG)
                    imageLink = obj.enclosureUrl

                return News(
                    title = obj.title ?: FeedListViewModel.UNDEFINED_MSG,
                    description = obj.description ?: FeedListViewModel.UNDEFINED_MSG,
                    date = obj.pubDate,
                    imageLink = imageLink,
                    originalLink = obj.link ?: ""
                )
            }
        }
    }
}