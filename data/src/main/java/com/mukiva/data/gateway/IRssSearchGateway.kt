package com.mukiva.data.gateway

import com.mukiva.data.entity.Rss

interface IRssSearchGateway {
    suspend fun search(url: String): Rss
}