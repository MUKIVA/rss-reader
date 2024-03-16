package com.mukiva.data.repository

import com.mukiva.data.store.rss_meta.entity.RssMetaEntity

interface IRssMetaRepository {

    suspend fun getAllRssMeta(): Collection<RssMetaEntity>
    suspend fun deleteRssMeta(url: String)
    suspend fun addRssMeta(
        url: String,
        originalUrl: String,
        name: String,
        description: String,
        imageUrl: String
    )

    suspend fun rssIsSaved(url: String): Boolean

}