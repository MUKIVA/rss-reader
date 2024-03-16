package com.mukiva.data

import com.mukiva.data.entity.Item
import com.mukiva.data.entity.Rss
import com.mukiva.data.gateway.IRssSearchGateway
import com.mukiva.data.repository.IRssRepository
import com.mukiva.data.store.rss.IRssStore
import javax.inject.Inject

class RssRepositoryImpl @Inject constructor(
    private val gateway: IRssSearchGateway,
    private val store: IRssStore
) : IRssRepository {

    override suspend fun search(url: String): Rss {
        return when {
            !store.containsUrl(url) -> {
                val remoteRss = gateway.search(url)
                store.saveRss(remoteRss, url)
                store.getRss(url)
            }
            store.shouldBeUpdated(url) ->
                updateItems(url)
            else -> store.getRss(url)
        }
    }

    override suspend fun getNewsById(id: Long): Item {
        return store.getRssItem(id)
    }

    override suspend fun updateItems(url: String): Rss {
        val remoteRss = gateway.search(url)
        store.updateRss(remoteRss, url)
        return store.getRss(url)
    }

    override suspend fun getNewsPage(url: String, page: Int, pageSize: Int): Collection<Item> {
        return when {
            !store.containsUrl(url) -> {
                val remoteRss = gateway.search(url)
                store.saveRss(remoteRss, url)
                store.getPage(url, page, pageSize)
            }
            store.shouldBeUpdated(url) && page == 0 -> {
                updateItems(url)
                store.getPage(url, page, pageSize)
            }
            else -> store.getPage(url, page, pageSize)
        }
    }
}