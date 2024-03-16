package com.mukiva.feature.feed.domain.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.feed.domain.repository.IRssRepository
import javax.inject.Inject

class GetRssMetaDataUseCase @Inject constructor(
    private val repository: IRssRepository
) {

    suspend operator fun invoke(): IResult<Collection<IRssMeta>> {
        return try {
            IResult.Success(repository.getAllRssMeta())
        } catch (e: Exception) {
            IResult.Error(IResultError.UnknownError)
        }

    }

}