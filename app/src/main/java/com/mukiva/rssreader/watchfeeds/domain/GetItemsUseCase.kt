package com.mukiva.rssreader.watchfeeds.domain

import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.domain.Error
import com.mukiva.rssreader.addrss.domain.Result
import com.mukiva.rssreader.addrss.domain.Success
import com.mukiva.rssreader.addrss.domain.UnknownError

class GetItemsUseCase(
    private val _rssStorage: RssStorage
) {
    suspend operator fun invoke(id: Long): Result<List<Item>> {
        return try {
            val list = _rssStorage.getItems(id)
            Success(list)
        } catch (e: Exception) {
            Error(UnknownError)
        }
    }
}