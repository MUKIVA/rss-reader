package com.mukiva.rssreader.glue.feed.navigation

import com.mukiva.feature.feed.navigation.IFeedRouter
import com.mukiva.feature.article_viewer.ui.ArticleViewerDestination
import com.mukiva.feature.search.impl.ui.SearchDestination
import com.mukiva.navigation.domain.IGlobalRouter
import javax.inject.Inject

class FeedRouter @Inject constructor(
    private val globalRouter: IGlobalRouter
) : IFeedRouter {
    override suspend fun goBack() {
        globalRouter.navigateUp()
    }

    override suspend fun goSearch() {
        globalRouter.navigateTo(SearchDestination)
    }

    override suspend fun goNewsViewer(id: Long) {
        globalRouter.navigateTo(
            destination = ArticleViewerDestination,
            args = listOf(id)
        )
    }
}