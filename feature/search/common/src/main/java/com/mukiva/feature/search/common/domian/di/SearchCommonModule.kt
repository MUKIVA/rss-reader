package com.mukiva.feature.search.common.domian.di

import com.mukiva.feature.search.common.presentation.SearchState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SearchCommonModule {

    @Provides
    fun provideSearchState() = SearchState.default()

}