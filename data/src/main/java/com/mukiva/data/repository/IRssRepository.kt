package com.mukiva.data.repository

import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Rss

interface IRssRepository {

    suspend fun search(url: String): Rss
    suspend fun getNewsById(id: Long): Item
    suspend fun updateItems(url: String): Rss
    suspend fun getNewsPage(url: String, page: Int, pageSize: Int): Collection<Item>

}