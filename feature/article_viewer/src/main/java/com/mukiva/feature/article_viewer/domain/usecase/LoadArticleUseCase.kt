package com.mukiva.feature.article_viewer.domain.usecase

import com.mukiva.core.utils.domain.IResult
import com.mukiva.core.utils.domain.IResultError
import com.mukiva.feature.article_viewer.domain.IArticle
import com.mukiva.feature.article_viewer.domain.repository.IArticleRepository
import javax.inject.Inject

class LoadArticleUseCase @Inject constructor(
    private val repository: IArticleRepository
) {

    suspend operator fun invoke(id: Long): IResult<IArticle> {
        return try {
            IResult.Success(repository.loadNewsById(id))
        } catch (e: Exception) {
            IResult.Error(IResultError.UnknownError)
        }
    }

}