package com.mukiva.rssreader.glue.feed.di

import com.mukiva.feature.feed.domain.repository.IRssRepository
import com.mukiva.feature.feed.navigation.IFeedRouter
import com.mukiva.rssreader.glue.feed.navigation.FeedRouter
import com.mukiva.rssreader.glue.feed.repository.AdapterRssRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface IFeedGlueBinds {

    @Binds
    fun bindFeedRouter(
        router: FeedRouter
    ): IFeedRouter

    @Binds
    fun bindRssRepoAdapter(
        adapter: AdapterRssRepository
    ): IRssRepository

}