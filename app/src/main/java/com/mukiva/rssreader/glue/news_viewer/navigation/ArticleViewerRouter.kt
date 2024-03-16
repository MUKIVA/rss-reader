package com.mukiva.rssreader.glue.news_viewer.navigation

import com.mukiva.feature.article_viewer.navigation.IArticleViewerRouter
import com.mukiva.navigation.domain.IGlobalRouter
import javax.inject.Inject

class ArticleViewerRouter @Inject constructor(
    private val globalRouter: IGlobalRouter
) : IArticleViewerRouter {
    override suspend fun goBack() {
        globalRouter.navigateUp()
    }
}