package com.mukiva.core.ui

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}