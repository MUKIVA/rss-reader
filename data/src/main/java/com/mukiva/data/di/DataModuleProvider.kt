package com.mukiva.data.di

import android.content.Context
import com.mukiva.data.store.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModuleProvider {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ) : BoxStore {
        return MyObjectBox
            .builder()
            .androidContext(context)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(null)
            .build()
    }

}