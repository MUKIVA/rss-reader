package com.mukiva.feature.search.common.domian.di

import com.mukiva.feature.search.common.presentation.ISearchStateHolder
import com.mukiva.feature.search.common.presentation.SearchSateHolder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ISearchCommonBinds {

    @Binds
    fun bindSearchStateHolder(
        holder: SearchSateHolder
    ): ISearchStateHolder

}