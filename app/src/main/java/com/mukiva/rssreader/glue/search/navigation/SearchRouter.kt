package com.mukiva.rssreader.glue.search.navigation

import com.mukiva.feature.search.impl.navigation.ISearchRouter
import com.mukiva.rssreader.glue.navigation.GlobalRouter
import javax.inject.Inject

class SearchRouter @Inject constructor(
    private val globalRouter: GlobalRouter
) : ISearchRouter {

    override suspend fun goBack() {
        globalRouter.navigateUp()
    }
}