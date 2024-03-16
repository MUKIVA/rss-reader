package com.mukiva.rssreader.glue.navigation

import com.mukiva.feature.feed.ui.FeedDestination
import com.mukiva.feature.article_viewer.ui.ArticleViewerDestination
import com.mukiva.feature.search.impl.ui.SearchDestination
import com.mukiva.navigation.domain.IDestination
import com.mukiva.navigation.domain.IDestinationsHolder
import javax.inject.Inject

class DestinationsHolder @Inject constructor(

) : IDestinationsHolder {
    override val startDestination: IDestination = FeedDestination
    override val destinations: Collection<IDestination> = listOf(
        FeedDestination,
        SearchDestination,
        ArticleViewerDestination
    )
}