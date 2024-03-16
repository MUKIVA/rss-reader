package com.mukiva.feature.article_viewer.di

import com.mukiva.feature.article_viewer.presentation.ArticleViewerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ArticleViewerModule {
    @Provides
    fun provideArticleViewerStateDefault() = ArticleViewerState.default()
}