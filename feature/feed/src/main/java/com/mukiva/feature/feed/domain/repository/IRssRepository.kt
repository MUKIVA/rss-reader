package com.mukiva.feature.feed.domain.repository

import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssMeta

interface IRssRepository {
    suspend fun requestRss(url: String): IRss
    suspend fun requestRssPage(url: String, page: Int, pageSize: Int): Collection<IArticle>
    suspend fun addRssMeta(meta: IRssMeta)
    suspend fun deleteRss(url: String)
    suspend fun getAllRssMeta(): Collection<IRssMeta>
    suspend fun forceUpdateRss(url: String): IRss
}