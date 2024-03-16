package com.mukiva.data

import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Rss
import com.mukiva.data.store.rss.IRssStore
import com.mukiva.data.store.rss.entity.ChannelEntity
import com.mukiva.data.store.rss.entity.ItemEntity
import com.mukiva.data.store.rss.entity.ItemEntity_
import com.mukiva.data.store.rss.entity.LocalCache
import com.mukiva.data.store.rss.entity.LocalCache_
import com.mukiva.data.store.rss.entity.RssEntity
import com.mukiva.data.store.rss.mapper.ItemsMapper
import com.mukiva.data.store.rss.mapper.RssMapper
import io.objectbox.BoxStore
import io.objectbox.kotlin.equal
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Integer.min
import java.util.Calendar
import javax.inject.Inject

class RssSearchStoreImpl @Inject constructor(
    private val store: BoxStore,
    private val rssMapper: RssMapper,
    private val itemMapper: ItemsMapper
) : IRssStore {

    private val mCalendar = Calendar.getInstance()
    private val mCacheBox = store.boxFor(LocalCache::class.java)
    private val mItemsBox = store.boxFor(ItemEntity::class.java)
    private val mChannelBox = store.boxFor(ChannelEntity::class.java)

    override suspend fun containsUrl(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val query = mCacheBox
                    .query()
                    .equal(LocalCache_.url, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                query.findFirst() != null
            }
        }
    }

    override suspend fun shouldBeUpdated(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val query = mCacheBox
                    .query()
                    .equal(LocalCache_.url, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val entity = query.findFirst()
                val lastUpdate = entity?.lastUpdate
                    ?: return@callInReadTx true

                val timeDiff = mCalendar.time.time - lastUpdate.time
                query.close()

                return@callInReadTx timeDiff >= TIME_FOR_UPDATE_HR
            }
        }
    }

    override suspend fun getRss(url: String): Rss {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val query = mCacheBox.query()
                    .equal(LocalCache_.url, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val rssEntity = query.findFirst()?.rssId

                if (rssEntity == null || rssEntity.isNull)
                    error("Fail to get RSS from storage")

                val rss = rssMapper.mapAsDomain(rssEntity.target)
                query.close()
                rss
            }
        }
    }

    override suspend fun saveRss(rss: Rss, url: String) {
        withContext(Dispatchers.IO) {
            store.runInTx {
                val entity = rssMapper.mapAsEntity(rss)
                val cacheEntity = LocalCache(
                    url = url,
                    lastUpdate = mCalendar.time
                )

                cacheEntity.rssId.target = entity

                mCacheBox.put(cacheEntity)
            }
        }
    }

    override suspend fun updateRss(rss: Rss, url: String) {
        withContext(Dispatchers.IO) {
            store.callInTx {
                val query = mCacheBox
                    .query()
                    .equal(LocalCache_.url, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val cache = query.findFirst()
                val savedRss = cache?.rssId

                if (savedRss == null || savedRss.isNull)
                    error("Fail to update rss cache")

                val remoteEntity = rssMapper.mapAsEntity(rss)
                val mergedRss = mergeRssData(remoteEntity, savedRss.target)

                val updatedCache = cache.copy(
                    lastUpdate = mCalendar.time
                )

                updatedCache.rssId.target = mergedRss
                mCacheBox.put(updatedCache)
                query.close()
            }
        }
    }

    override suspend fun getRssItem(id: Long): Item {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val query = mItemsBox
                    .query()
                    .equal(ItemEntity_.id, id)
                    .build()

                val item = query.findFirst() ?: error("fail to get rss item with id: $id")
                query.close()
                itemMapper.mapAsDomain(item)
            }
        }
    }

    override suspend fun getPage(url: String, page: Int, pageSize: Int): Collection<Item> {
        return withContext(Dispatchers.IO) {
            store.callInReadTx {
                val offset = page * pageSize
                val query = mCacheBox
                    .query()
                    .equal(LocalCache_.url, url, QueryBuilder.StringOrder.CASE_INSENSITIVE)
                    .build()

                val channelId = query.findFirst()?.rssId?.target?.channel?.target?.id
                    ?: error("Not found channel")
                query.close()

                val itemsQuery = mItemsBox.query(
                    ItemEntity_.channelId equal channelId
                ).order(ItemEntity_.pubDate, QueryBuilder.DESCENDING).build()

                val items = itemsQuery.find().let {
                    if (offset >= it.size) return@let emptyList()
                    it.subList(offset, min(offset + pageSize, it.size - 1))
                }
                itemsQuery.close()
                items.map { itemMapper.mapAsDomain(it) }
            }
        }
    }

    private fun mergeRssData(remote: RssEntity, source: RssEntity): RssEntity {
        val remoteChannel = remote.channel.target
        val sourceChannel = source.channel.target

        val remoteItems = remoteChannel.items.toList()
        val sourceItems = sourceChannel.items.toList()

        val combinedItems = (sourceItems + remoteItems)
            .distinctBy { it.guid.target.text }

        sourceChannel.items.clear()
        sourceChannel.items.addAll(combinedItems)

        source.channel.target = sourceChannel
        mChannelBox.put(sourceChannel)
        return source
    }

    companion object {
        private const val TIME_FOR_UPDATE_HR = 1L * 60L * 60L * 1000L
    }
}