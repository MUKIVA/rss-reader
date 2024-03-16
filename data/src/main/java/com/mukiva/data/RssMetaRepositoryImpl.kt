package com.mukiva.data

import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.data.store.rss_meta.IRssMetaStore
import com.mukiva.data.store.rss_meta.entity.RssMetaEntity
import javax.inject.Inject

class RssMetaRepositoryImpl @Inject constructor(
    private val store: IRssMetaStore
) : IRssMetaRepository {
    override suspend fun getAllRssMeta(): Collection<RssMetaEntity> {
        return store.getAllRssMeta()
    }

    override suspend fun deleteRssMeta(url: String) {
        store.deleteRssMeta(url)
    }

    override suspend fun addRssMeta(
        url: String,
        originalUrl: String,
        name: String,
        description: String,
        imageUrl: String
    ) {
        if (store.containsUrl(originalUrl)) {
            throw IllegalArgumentException("URL already added")
        }

        store.saveRssMeta(url, originalUrl, name, description, imageUrl)
    }

    override suspend fun rssIsSaved(url: String): Boolean {
        return store.containsUrl(url)
    }
}