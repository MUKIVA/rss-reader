package com.mukiva.feature.feed.domain.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.feed.domain.repository.IRssRepository
import javax.inject.Inject

class DeleteRssUseCase @Inject constructor(
    private val repo: IRssRepository
) {
    suspend operator fun invoke(rss: IRssMeta): IResult<Unit> {
        return try {
            repo.deleteRss(rss.originalUrl)
            IResult.Success(Unit)
        } catch (e: Exception) {
            IResult.Error(IResultError.UnknownError)
        }
    }

}