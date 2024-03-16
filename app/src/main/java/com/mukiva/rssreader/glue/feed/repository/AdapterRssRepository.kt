package com.mukiva.rssreader.glue.feed.repository

import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.feed.domain.repository.IRssRepository
import com.mukiva.data.repository.IRssRepository as IGlobalRepo
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.rssreader.glue.feed.mapper.RssMapper
import javax.inject.Inject

class AdapterRssRepository @Inject constructor(
    private val coreSearchRepo: IGlobalRepo,
    private val coreMetaRepo: IRssMetaRepository
) : IRssRepository {

    override suspend fun requestRss(url: String): IRss {
        return RssMapper.map(coreSearchRepo.search(url), url)
    }

    override suspend fun requestRssPage(
        url: String,
        page: Int,
        pageSize: Int
    ): Collection<IArticle> {
        return coreSearchRepo.getNewsPage(url, page, pageSize).map { RssMapper.mapItem(it) }
    }

    override suspend fun addRssMeta(meta: IRssMeta) {
        coreMetaRepo.addRssMeta(
            meta.url,
            meta.originalUrl,
            meta.name,
            meta.description,
            meta.imageUrl
        )
    }

    override suspend fun deleteRss(url: String) {
        coreMetaRepo.deleteRssMeta(url)
    }

    override suspend fun getAllRssMeta(): Collection<IRssMeta> {
        return coreMetaRepo.getAllRssMeta().map { meta ->
            object : IRssMeta {
                override val name: String
                    get() = meta.name
                override val url: String
                    get() = meta.url
                override val originalUrl: String
                    get() = meta.originalUrl
                override val description: String
                    get() = meta.description
                override val imageUrl: String
                    get() = meta.imageUrl
            }
        }
    }

    override suspend fun forceUpdateRss(url: String): IRss {
        return RssMapper.map(coreSearchRepo.updateItems(url), url)
    }

}