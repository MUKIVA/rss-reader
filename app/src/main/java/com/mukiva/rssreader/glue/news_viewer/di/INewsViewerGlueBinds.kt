package com.mukiva.rssreader.glue.news_viewer.di

import com.mukiva.feature.article_viewer.domain.repository.IArticleRepository
import com.mukiva.feature.article_viewer.navigation.IArticleViewerRouter
import com.mukiva.rssreader.glue.news_viewer.navigation.ArticleViewerRouter
import com.mukiva.rssreader.glue.news_viewer.repository.AdapterArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface INewsViewerGlueBinds {

    @Binds
    fun bindNewsRepository(
        repo: AdapterArticleRepository
    ): IArticleRepository

    @Binds
    fun bindNewsViewerRouter(
        router: ArticleViewerRouter
    ): IArticleViewerRouter

}