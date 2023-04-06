package com.mukiva.rssreader.watchfeeds.domain

import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.data.parsing.elements.Rss

interface RssStorage {
    suspend fun update(rss: Rss, id: Long): Channel
    suspend fun delete(id: Long): List<Channel>
    suspend fun add(rss: Rss): List<Channel>
    suspend fun getAllRss(): List<Channel>
    suspend fun getItems(id: Long): List<Item>
    suspend fun getRss(id: Long): Channel
    suspend fun getItem(id: Long): Item

}