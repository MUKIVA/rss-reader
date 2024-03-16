package com.mukiva.feature.feed.navigation

interface IFeedRouter {
    suspend fun goBack()
    suspend fun goSearch()
    suspend fun goNewsViewer(id: Long)
}