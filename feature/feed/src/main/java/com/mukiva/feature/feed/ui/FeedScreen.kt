@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.mukiva.feature.feed.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.mukiva.core.ui.ErrorScreen
import com.mukiva.core.ui.LoadingScreen
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.feed.R
import com.mukiva.core.ui.R as CoreUiRes
import com.mukiva.feature.feed.presentation.FeedPage
import com.mukiva.feature.feed.presentation.FeedScreenState
import com.mukiva.feature.feed.presentation.FeedViewModel
import com.mukiva.feature.feed.presentation.IFeedEvent
import com.mukiva.feature.search.common.ui.StatelessSearch
import com.mukiva.navigation.domain.IDestination
import kotlinx.coroutines.launch

object FeedDestination : IDestination.IEmptyArgsDestination {
    override val route: String
        get() = "feed"
    override val screen: @Composable () -> Unit
        get() = {
            val viewModel = hiltViewModel<FeedViewModel>()
            val ctx = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            val state = viewModel.stateFlow.collectAsState()
            val searchState = viewModel.searchStateFlow.collectAsState()

            var showMenuState by rememberSaveable {
                mutableStateOf(false)
            }
            var showBottomSheet by rememberSaveable {
                mutableStateOf(false)
            }
            var showDeleteDialog by rememberSaveable {
                mutableStateOf(false)
            }
            val pagerState = rememberPagerState {
                state.value.asPagerState.pageCount
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {}
            )

            LaunchedEffect(viewModel) {
                launch {
                    viewModel.eventFlow
                        .flowWithLifecycle(lifecycleOwner.lifecycle)
                        .collect { event ->
                            when(event) {
                                IFeedEvent.InternetConnectionError -> Toast.makeText(
                                    ctx,
                                    CoreUiRes.string.search_error_network,
                                    Toast.LENGTH_SHORT
                                ).show()
                                IFeedEvent.UnknownError -> Toast.makeText(
                                    ctx,
                                    CoreUiRes.string.unknown_error,
                                    Toast.LENGTH_SHORT
                                ).show()

                                is IFeedEvent.ScrollToPage ->
                                    pagerState.scrollToPage(event.pageIndex, 0f)
                            }
                        }
                }
            }

            Scaffold(
                topBar = {
                    if (state.value.type != FeedScreenState.Type.LOADING)
                        RSSReaderAppBar(
                            title = ctx.getString(R.string.app_name),
                            pages = state.value.pages,
                            aboutFeedText = ctx.getString(R.string.menu_about_feed),
                            addFeedText = ctx.getString(R.string.menu_add_feed),
                            deleteFeedText = ctx.getString(R.string.menu_delete_feed),
                            onDeleteFeed = {
                                showDeleteDialog = true
                                showMenuState = false
                                           },
                            onAddFeed = {
                                viewModel.goSearch()
                                showMenuState = false
                                        },
                            onAboutFeed = {
                                showBottomSheet = true
                                showMenuState = false
                                          },
                            showMenu = showMenuState,
                            onMenuClick = { showMenuState = !showMenuState },
                            selectedTabIndex = pagerState.currentPage,
                            onTabClick = viewModel::scrollToPage
                        )
                },
                modifier = Modifier.imePadding()
            ) {
                Surface(modifier = Modifier.padding(it)) {
                    when (state.value.type) {
                        FeedScreenState.Type.LOADING -> {
                            LoadingScreen()
                        }
                        FeedScreenState.Type.EMPTY -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                StatelessSearch(
                                    noteText = ctx.getString(R.string.add_first_rss_text),
                                    searchState = searchState.value,
                                    placeholder = ctx.getString(CoreUiRes.string.search_placeholder),
                                    onAddClick = { viewModel.addRss() },
                                    onTextChange = { value -> viewModel.updateValue(value) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                        FeedScreenState.Type.ERROR -> {
                            ErrorScreen(
                                text = ctx.getString(R.string.fail_get_data_text),
                                buttonText = ctx.getString(CoreUiRes.string.repeat),
                                onButtonClick = { viewModel.run {} },
                                image = Icons.Rounded.Warning,
                                imageColor = ColorFilter.tint(MaterialTheme.colorScheme.error)
                            )
                        }
                        FeedScreenState.Type.CONTENT -> {
                            StatelessFeedScreen(
                                pages = state.value.pages,
                                pagerState = pagerState,
                                onPageLoading = { pageIndex ->
                                    viewModel.loadNextPage(pageIndex)
                                },
                                onOpenNews = { newsId ->
                                    viewModel.goNewsViewer(newsId)
                                },
                                onRefresh = viewModel::forceUpdateFeed
                            )
                        }
                    }
                }
            }
            if (showBottomSheet) {
                val currentPage = pagerState.currentPage
                val page = state.value.pages.elementAt(currentPage)
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false }
                ) {
                    StatelessAboutRssItem(
                        title = page.name,
                        description = page.description,
                        buttonText = ctx.getString(R.string.details_open_original),
                        onButtonClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(page.url)
                            }
                            launcher.launch(intent)
                        }
                    )
                }
            }

            if (showDeleteDialog) {
                val currentPage = pagerState.currentPage
                val page = state.value.pages.elementAt(currentPage)
                AlertDialog(
                    text = {
                        val alert = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append(ctx.getString(R.string.alert_delete_rss))
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(" ${page.name}?")
                            }
                        }
                        Text(text = alert)
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteRss(currentPage)
                            showDeleteDialog = false
                        }) {
                            Text(
                                text = ctx.getString(CoreUiRes.string.delete),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(
                                text = ctx.getString(CoreUiRes.string.cancel)
                            )
                        }
                    },
                    onDismissRequest = { showDeleteDialog = false }
                )
            }
        }
}


@Composable
fun StatelessFeedScreen(
    pages: Collection<FeedPage>,
    pagerState: PagerState,
    onPageLoading: (Int) -> Unit,
    onOpenNews: (Long) -> Unit,
    onRefresh: suspend (Int) -> Unit
) {
    StatelessNewsPager(
        pagerState = pagerState,
        pages = pages,
        onLoadList = onPageLoading,
        onOpenNews = onOpenNews,
        onRefresh = onRefresh
    )
}


@Composable
@Preview
@DebugOnly
fun PreviewFeedScreen() {
    val mock = FeedScreenState.mock()
    StatelessFeedScreen(
        pages = mock.pages,
        pagerState = mock.asPagerState,
        onPageLoading = {},
        onOpenNews = {},
        onRefresh = {}
    )
}