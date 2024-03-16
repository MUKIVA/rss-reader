package com.mukiva.feature.search.common.domian.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.search.common.domian.repository.IRssRepository
import javax.inject.Inject

class AddRssUseCase @Inject constructor(
    private val repo: IRssRepository
) {
    suspend operator fun invoke(rss: IRssMeta): IResult<Unit> {
        return try {
            repo.addRssMeta(rss)
            IResult.Success(Unit)
        } catch (e: Exception) {
            IResult.Error(IResultError.UnknownError)
        }
    }

}