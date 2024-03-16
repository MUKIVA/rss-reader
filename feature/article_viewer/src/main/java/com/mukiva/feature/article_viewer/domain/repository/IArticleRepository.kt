package com.mukiva.feature.article_viewer.domain.repository

import com.mukiva.feature.article_viewer.domain.IArticle

interface IArticleRepository {
    suspend fun loadNewsById(id: Long): IArticle

}