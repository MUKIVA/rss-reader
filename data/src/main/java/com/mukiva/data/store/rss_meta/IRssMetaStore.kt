package com.mukiva.data.store.rss_meta

import com.mukiva.data.store.rss_meta.entity.RssMetaEntity

interface IRssMetaStore {
    suspend fun getAllRssMeta(): List<RssMetaEntity>
    suspend fun deleteRssMeta(url: String)
    suspend fun containsUrl(url: String): Boolean
    suspend fun saveRssMeta(
        url: String,
        originalUrl: String,
        name: String,
        description: String,
        imageUrl: String
    )
}