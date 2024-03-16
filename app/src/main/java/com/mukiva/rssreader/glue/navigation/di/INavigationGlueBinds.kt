package com.mukiva.rssreader.glue.navigation.di

import com.mukiva.navigation.domain.IDestinationsHolder
import com.mukiva.navigation.domain.IGlobalRouter
import com.mukiva.rssreader.glue.navigation.DestinationsHolder
import com.mukiva.rssreader.glue.navigation.GlobalRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent


@Module
@InstallIn(ActivityRetainedComponent::class)
interface INavigationGlueBinds {

    @Binds
    fun bindNavigationDestinationsHolder(
        holder: DestinationsHolder
    ): IDestinationsHolder

    @Binds
    fun bindGlobalRouter(
        router: GlobalRouter
    ): IGlobalRouter

}