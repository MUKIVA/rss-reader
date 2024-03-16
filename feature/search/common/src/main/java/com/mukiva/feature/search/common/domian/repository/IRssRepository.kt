package com.mukiva.feature.search.common.domian.repository

import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.IRssMeta

interface IRssRepository {
    suspend fun addRssMeta(meta: IRssMeta)
    suspend fun requestRss(url: String): IRss
    suspend fun urlIsContains(url: String): Boolean
}