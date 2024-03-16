package com.mukiva.feature.search.common.domian.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.search.common.domian.IRss
import com.mukiva.feature.search.common.domian.repository.IRssRepository
import kotlinx.coroutines.TimeoutCancellationException
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

class RequestRssItemsUseCase @Inject constructor(
    private val repository: IRssRepository
) {

    suspend operator fun invoke(url: String, checkMetaContains: Boolean = false): IResult<IRss> {
        var lowUrl = url.lowercase()
        if (!lowUrl.matches(Regex("^(https://).*$")))
            lowUrl = "https://$lowUrl"

        if (!isValidUrl(lowUrl)) return IResult.Error(IResultError.InvalidUrlError)

        if (checkMetaContains && repository.urlIsContains(lowUrl))
            return IResult.Error(IResultError.UrlAlreadyExists)

        return try {
            IResult.Success(repository.requestRss(lowUrl))
        } catch (e: Exception) {
            when(e) {
                is TimeoutCancellationException ->
                    IResult.Error(IResultError.TimeoutError)
                is IllegalArgumentException ->
                    IResult.Error(IResultError.InvalidUrlError)
                else -> IResult.Error(IResultError.UnknownError)
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return url.toHttpUrlOrNull() != null
    }

}