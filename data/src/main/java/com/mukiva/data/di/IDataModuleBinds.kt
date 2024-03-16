package com.mukiva.data.di

import com.mukiva.data.RssMetaRepositoryImpl
import com.mukiva.data.RssMetaStoreImpl
import com.mukiva.data.RssSearchGatewayImpl
import com.mukiva.data.RssRepositoryImpl
import com.mukiva.data.RssSearchStoreImpl
import com.mukiva.data.gateway.IRssSearchGateway
import com.mukiva.data.repository.IRssMetaRepository
import com.mukiva.data.repository.IRssRepository
import com.mukiva.data.store.rss_meta.IRssMetaStore
import com.mukiva.data.store.rss.IRssStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IDataModuleBinds {

    @Binds
    fun bindRssSearchGateway(
        gateway: RssSearchGatewayImpl
    ): IRssSearchGateway

    @Binds
    fun bindRssSearchStore(
        store: RssSearchStoreImpl
    ): IRssStore

    @Binds
    fun bindRssRepository(
        repo: RssRepositoryImpl
    ): IRssRepository

    @Binds
    fun bindRssMetaStore(
        store: RssMetaStoreImpl
    ): IRssMetaStore

    @Binds
    fun bindRssMetaRepository(
        repo: RssMetaRepositoryImpl
    ): IRssMetaRepository
}