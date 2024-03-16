@file:OptIn(ExperimentalMaterial3Api::class)

package com.mukiva.feature.article_viewer.ui

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import com.mukiva.core.ui.ErrorScreen
import com.mukiva.core.ui.LoadingScreen
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.article_viewer.presentation.IArticleViewerEvent
import com.mukiva.feature.article_viewer.presentation.ArticleViewerState
import com.mukiva.feature.article_viewer.presentation.ArticleViewerViewModel
import com.mukiva.navigation.domain.IDestination.IArgumentDestination
import com.mukiva.navigation.ui.getValue
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import com.mukiva.core.ui.R as CoreUiRes

@Parcelize
data class Args(
    val newsId: Long
) : Parcelable

object ArticleViewerDestination : IArgumentDestination  {

    private const val KEY_ID = "id"

    override val argsType: Collection<Pair<String, NavType<*>>>
        get() = listOf(
            KEY_ID to NavType.LongType
        )
    override val route: String
        get() = "news_viewer"
    override val screen: @Composable (List<Pair<String, Any?>>?) -> Unit
        get() = @Composable { args ->

            val vm = hiltViewModel<ArticleViewerViewModel>()
            val ctx = LocalContext.current
            val id = args?.getValue(KEY_ID) as Long
            val state = vm.stateFlow.collectAsState()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {}
            )

            StatelessArticleViewerScreen(
                state = state.value,
                onErrorRepeat = { vm.load(id) },
                onNavigateUp = vm::onBack,
                onShare = vm::share,
                onOpenOriginal = vm::openOriginal
            )

            LaunchedEffect(vm) {
                vm.load(id)

                launch {
                    vm.eventFlow.collect { event -> when(event) {
                        is IArticleViewerEvent.OpenOriginal -> {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(event.url)
                            }
                            launcher.launch(intent)
                        }
                        is IArticleViewerEvent.Share -> {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, event.url)
                                type = "text/plain"
                            }
                            val chooser = Intent.createChooser(intent, null)
                            ctx.startActivity(chooser)
                        }
                    }}
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessArticleViewerScreen(
    state: ArticleViewerState,
    onErrorRepeat: () -> Unit,
    onNavigateUp: () -> Unit,
    onShare: () -> Unit,
    onOpenOriginal: () -> Unit
) {
    val ctx = LocalContext.current

    when(state.type) {
        ArticleViewerState.Type.LOADING -> LoadingScreen()
        ArticleViewerState.Type.CONTENT -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onNavigateUp) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.ArrowBack,
                                    "")
                            }
                        },
                        actions = {
                            IconButton(onClick = onShare) {
                                Icon(
                                    Icons.Rounded.Share,
                                    "")
                            }
                        }
                    )
                }
            ) {
                StatelessArticleViewerContent(
                    state = state.news,
                    onOpenOriginal = onOpenOriginal,
                    horizontalContentPadding = 16.dp,
                    modifier = Modifier
                        .padding(it)
                )
            }
        }
        ArticleViewerState.Type.ERROR -> ErrorScreen(
            text = ctx.getString(CoreUiRes.string.unknown_error),
            buttonText = ctx.getString(CoreUiRes.string.repeat),
            onButtonClick = onErrorRepeat
        )
    }
}

@Composable
@Preview
@DebugOnly
fun PreviewArticleViewerScreen() {
    MaterialTheme {
        StatelessArticleViewerScreen(
            state = ArticleViewerState.mock(),
            onErrorRepeat = {},
            onNavigateUp = {},
            onShare = {},
            onOpenOriginal = {}
        )
    }
}