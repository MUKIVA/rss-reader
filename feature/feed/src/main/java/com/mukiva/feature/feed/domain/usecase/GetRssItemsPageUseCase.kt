package com.mukiva.feature.feed.domain.usecase

import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.feed.domain.repository.IRssRepository
import javax.inject.Inject

class GetRssItemsPageUseCase @Inject constructor(
    private val repository: IRssRepository
) {
    suspend operator fun invoke(url: String, page: Int, pageSize: Int): Result<Collection<IArticle>> {
        return try {
            val result = repository.requestRssPage(url, page, pageSize)
            return Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}