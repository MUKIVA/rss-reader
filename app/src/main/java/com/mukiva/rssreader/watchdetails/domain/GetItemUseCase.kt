package com.mukiva.rssreader.watchdetails.domain

import com.mukiva.rssreader.addrss.data.parsing.elements.Item
import com.mukiva.rssreader.addrss.domain.Error
import com.mukiva.rssreader.addrss.domain.Result
import com.mukiva.rssreader.addrss.domain.Success
import com.mukiva.rssreader.addrss.domain.UnknownError
import com.mukiva.rssreader.watchfeeds.domain.RssStorage

class GetItemUseCase(
    private val _rssStorage: RssStorage
) {
    suspend operator fun invoke(id: Long): Result<Item> {
        return try {
            val item = _rssStorage.getItem(id)
            Success(item)
        } catch (e: Exception) {
            Error(UnknownError)
        }
    }
}