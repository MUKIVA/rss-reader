package com.mukiva.data

import com.mukiva.data.store.rss_meta.IRssMetaStore
import com.mukiva.data.store.rss_meta.entity.RssMetaEntity
import com.mukiva.data.store.rss_meta.entity.RssMetaEntity_
import io.objectbox.BoxStore
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RssMetaStoreImpl @Inject constructor(
    private val store: BoxStore
) : IRssMetaStore {

    private val mMetaStoreBox = store.boxFor(RssMetaEntity::class.java)

    override suspend fun getAllRssMeta(): List<RssMetaEntity> {
        return withContext(Dispatchers.IO) {
            store.callInReadTx { mMetaStoreBox.all }
        }
    }

    override suspend fun deleteRssMeta(url: String) {
        withContext(Dispatchers.IO) {
            store.runInTx {
                val query = mMetaStoreBox
                    .query()
                    .equal(RssMetaEntity_.originalUrl, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val meta = query.findFirst()
                    ?: error("Fail to remove rss meta")

                query.close()

                mMetaStoreBox.remove(meta.id)
            }
        }
    }

    override suspend fun containsUrl(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val query = mMetaStoreBox
                    .query()
                    .equal(RssMetaEntity_.originalUrl, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val meta = query.findFirst()

                query.close()

                meta != null
            }
        }
    }

    override suspend fun saveRssMeta(
        url: String,
        originalUrl: String,
        name: String,
        description: String,
        imageUrl: String
    ) {
        withContext(Dispatchers.IO) {
            store.runInTx {
                val meta = RssMetaEntity(
                    url = url,
                    originalUrl = originalUrl,
                    name = name,
                    description = description,
                    imageUrl = imageUrl
                )
                mMetaStoreBox.put(meta)
            }
        }
    }
}