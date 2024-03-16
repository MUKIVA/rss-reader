package com.mukiva.rssreader.glue.search.di

import com.mukiva.feature.search.impl.navigation.ISearchRouter
import com.mukiva.rssreader.glue.search.navigation.SearchRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ISearchGlueBinds {

    @Binds
    fun bindSearchRouter(
        router: SearchRouter
    ): ISearchRouter

}