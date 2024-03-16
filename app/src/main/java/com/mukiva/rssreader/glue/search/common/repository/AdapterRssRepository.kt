package com.mukiva.rssreader.glue.search.common.repository

import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.data.repository.IRssRepository as IGlobalRepo
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.search.common.domian.repository.IRssRepository
import com.mukiva.rssreader.glue.feed.mapper.RssMapper
import javax.inject.Inject

class AdapterRssRepository @Inject constructor(
    private val coreSearchRepo: IGlobalRepo,
    private val coreMetaRepo: IRssMetaRepository
) : IRssRepository {
    override suspend fun addRssMeta(meta: IRssMeta) {
        coreMetaRepo.addRssMeta(
            url = meta.url,
            originalUrl = meta.originalUrl,
            name = meta.name,
            description = meta.description,
            imageUrl = meta.imageUrl
        )
    }

    override suspend fun requestRss(url: String): IRss {
        return RssMapper.map(coreSearchRepo.search(url), url)
    }

    override suspend fun urlIsContains(url: String): Boolean {
        return coreMetaRepo.rssIsSaved(url)
    }
}