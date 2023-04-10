package com.mukiva.rssreader.watchfeeds.domain

import com.mukiva.rssreader.addrss.data.parsing.elements.Channel
import com.mukiva.rssreader.addrss.domain.Error
import com.mukiva.rssreader.addrss.domain.Result
import com.mukiva.rssreader.addrss.domain.Success
import com.mukiva.rssreader.addrss.domain.UnknownError

class GetAllRssUseCase(
    private val _rssStorage: RssStorage
) {
    suspend operator fun invoke(): Result<List<Channel>> {
        return try {
            val list = _rssStorage.getAllRss()
            Success(list)
        } catch (e: Exception) {
            Error(UnknownError)
        }
    }
}