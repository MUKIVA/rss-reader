package com.mukiva.feature.feed.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukiva.core.ui.DateFormatter
import com.mukiva.core.ui.EmptyScreen
import com.mukiva.core.ui.ErrorScreen
import com.mukiva.core.ui.LoadingScreen
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.feed.R
import com.mukiva.core.ui.R as CoreUiRes
import com.mukiva.feature.feed.presentation.FeedPage
import com.mukiva.feature.feed.presentation.FeedScreenState
import com.mukiva.feature.feed.domain.IArticle
import com.mukiva.feature.feed.presentation.FeedPagnator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatefulNewsList(
    type: FeedPage.Type,
    list: Collection<IArticle>,
    loaderState: FeedPagnator.ProgressType,
    onItemClick: (Long) -> Unit,
    onRefresh: suspend () -> Unit,
    onLoadList: () -> Unit,
) {
    val listState = rememberLazyListState()
    val swipeRefreshState = rememberPullToRefreshState()

    if (swipeRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
            swipeRefreshState.endRefresh()
        }
    }

    StatelessNewsList(
        type = type,
        list = list,
        onLoadList = onLoadList,
        listState = listState,
        onItemClick = onItemClick,
        loaderState = loaderState,
        refreshState = swipeRefreshState
    )

    LaunchedEffect(null) {
        onLoadList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessNewsList(
    type: FeedPage.Type,
    list: Collection<IArticle>,
    loaderState: FeedPagnator.ProgressType,
    listState: LazyListState,
    refreshState: PullToRefreshState,
    onItemClick: (Long) -> Unit,
    onLoadList: () -> Unit
) {
    val ctx = LocalContext.current
    when(type) {
        FeedPage.Type.EMPTY -> EmptyScreen(
            ctx.getString(R.string.feed_is_empty)
        )
        FeedPage.Type.LOADING -> LoadingScreen()
        FeedPage.Type.CONTENT -> Box(modifier = Modifier
            .nestedScroll(refreshState.nestedScrollConnection)
            .fillMaxWidth()
        ) {
            if (refreshState.isRefreshing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                LinearProgressIndicator(
                    progress = { refreshState.progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LazyColumn(state = listState) {
                items(count = list.size) {
                    val pdValues = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    if (it >= list.size - 1
                        && loaderState != FeedPagnator.ProgressType.LOADING
                        && loaderState != FeedPagnator.ProgressType.ERROR) {
                        onLoadList()
                    }
                    val item = list.elementAt(it)
                    StatelessNewsItem(
                        label = item.title,
                        body = item.body,
                        date = DateFormatter.formatAsDayOfYear(item.date),
                        imgUrl = item.imgUrl,
                        modifier = Modifier.padding(pdValues),
                        onClick = { onItemClick(item.id) }
                    )
                }
                item {
                    when (loaderState) {
                        FeedPagnator.ProgressType.ERROR -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = ctx.getString(R.string.smth_went_wrong),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1.0f)
                                )
                                Button(
                                    onClick = onLoadList,
                                    modifier = Modifier.weight(0.5f)
                                ) {
                                    Text(text = ctx.getString(CoreUiRes.string.repeat))
                                }
                            }
                        }
                        FeedPagnator.ProgressType.LOADING -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        FeedPage.Type.ERROR -> ErrorScreen(
            text = ctx.getString(R.string.fail_get_data_text),
            buttonText = ctx.getString(com.mukiva.core.ui.R.string.repeat),
            onButtonClick = { onLoadList() },
            image = Icons.Rounded.Warning,
            imageColor = ColorFilter.tint(MaterialTheme.colorScheme.error)
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(
    apiLevel = 33,
    showBackground = true
)
@DebugOnly
fun PreviewNewsList() {
    val mock = FeedScreenState.mock()
    MaterialTheme {
        StatelessNewsList(
            list = mock.pages.elementAt(0).articleStateCollection,
            onLoadList = {},
            type = FeedPage.Type.CONTENT,
            listState = rememberLazyListState(),
            onItemClick = {},
            loaderState = FeedPagnator.ProgressType.ERROR,
            refreshState = rememberPullToRefreshState()
        )
    }
}