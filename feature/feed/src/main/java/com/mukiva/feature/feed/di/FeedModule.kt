package com.mukiva.feature.feed.di

import com.mukiva.feature.feed.presentation.FeedScreenState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FeedModule {

    @Provides
    fun provideFeedScreenState() = FeedScreenState.default()

}