package com.mukiva.rssreader.watchfeeds.data

import com.mukiva.rssreader.addrss.data.parsing.elements.Rss
import com.mukiva.rssreader.addrss.data.parsing.entity.ChannelEntity
import com.mukiva.rssreader.addrss.data.parsing.entity.ItemEntity

interface RssService {
    suspend fun updateRss(rss: Rss, id: Long): ChannelEntity
    suspend fun deleteRss(rss: ChannelEntity): List<ChannelEntity>
    suspend fun addRss(rss: Rss): List<ChannelEntity>
    suspend fun getAllRss(): List<ChannelEntity>
    suspend fun getChannelItems(rss: ChannelEntity): List<ItemEntity>

}