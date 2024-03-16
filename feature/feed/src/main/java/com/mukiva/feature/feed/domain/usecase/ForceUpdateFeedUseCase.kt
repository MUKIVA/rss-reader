package com.mukiva.feature.feed.domain.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.feed.domain.repository.IRssRepository
import com.mukiva.feature.search.common.domian.IRss
import kotlinx.coroutines.TimeoutCancellationException
import java.io.IOException
import javax.inject.Inject

class ForceUpdateFeedUseCase @Inject constructor(
    private val repository: IRssRepository
) {

    suspend operator fun invoke(url: String): IResult<IRss> {
        return try {
            val result = repository.forceUpdateRss(url)
            IResult.Success(result)
        } catch (e: Exception) {
            when(e) {
                is IOException -> IResult.Error(IResultError.ConnectionError)
                is IllegalArgumentException -> IResult.Error(IResultError.InvalidUrlError)
                is TimeoutCancellationException -> IResult.Error(IResultError.TimeoutError)
                else -> IResult.Error(IResultError.UnknownError)
            }
        }
    }

}