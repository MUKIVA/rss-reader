package com.mukiva.rssreader.glue.search.common.di

import com.mukiva.feature.search.common.domian.repository.IRssRepository
import com.mukiva.rssreader.glue.search.common.repository.AdapterRssRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ISearchGlueBinds {

    @Binds
    fun bindSearchRepository(
        repo: AdapterRssRepository
    ): IRssRepository

}