package com.mukiva.feature.feed.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.feed.presentation.FeedPage
import com.mukiva.feature.feed.presentation.FeedScreenState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatelessNewsPager(
    pagerState: PagerState,
    pages: Collection<FeedPage>,
    onLoadList: (Int) -> Unit,
    onOpenNews: (Long) -> Unit,
    onRefresh: suspend (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) {
        val listState = pages.elementAt(it)

        StatefulNewsList(
            list = listState.articleStateCollection,
            onLoadList = { onLoadList(it) },
            type = listState.type,
            onItemClick = onOpenNews,
            loaderState = listState.bottomProgressType,
            onRefresh = { onRefresh(it) }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview(
    apiLevel = 33
)
@DebugOnly
fun PreviewNewsPager() {
    MaterialTheme {
        val mock = FeedScreenState.mock()
        StatelessNewsPager(
            pagerState = mock.asPagerState,
            pages = mock.pages,
            onLoadList = {},
            onOpenNews = {},
            onRefresh = {}
        )
    }
}