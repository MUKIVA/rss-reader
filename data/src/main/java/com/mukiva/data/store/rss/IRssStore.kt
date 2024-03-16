package com.mukiva.data.store.rss

import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Rss

interface IRssStore {

    suspend fun containsUrl(url: String): Boolean
    suspend fun shouldBeUpdated(url: String): Boolean
    suspend fun getRss(url: String): Rss
    suspend fun saveRss(rss: Rss, url: String)
    suspend fun updateRss(rss: Rss, url: String)
    suspend fun getRssItem(id: Long): Item
    suspend fun getPage(url: String, page: Int, pageSize: Int): Collection<Item>

}