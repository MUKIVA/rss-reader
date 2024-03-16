package com.mukiva.feature.search.impl.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.search.common.ui.StatelessSearch
import com.mukiva.feature.search.impl.R
import com.mukiva.core.ui.R as CoreUiRes
import com.mukiva.feature.search.impl.presentation.ISearchEvent
import com.mukiva.feature.search.impl.presentation.SearchVewModel
import com.mukiva.navigation.domain.IDestination
import kotlinx.coroutines.launch

object SearchDestination : IDestination.IEmptyArgsDestination {
    override val route: String
        get() = "search"
    override val screen: @Composable () -> Unit
        get() = @Composable {
            SearchScreen()
        }

}

@Composable
fun SearchScreen() {
    val ctx = LocalContext.current
    val viewModel = hiltViewModel<SearchVewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    val searchState = viewModel.searchStateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        launch {
            viewModel.eventFlow
                .flowWithLifecycle(lifecycleOwner.lifecycle)
                .collect {
                    when(it) {
                        ISearchEvent.NoConnection -> Toast.makeText(
                            ctx,
                            CoreUiRes.string.search_error_network,
                            Toast.LENGTH_SHORT
                        ).show()
                        ISearchEvent.SuccessAdd -> Toast.makeText(
                            ctx,
                            R.string.success_add,
                            Toast.LENGTH_SHORT
                        ).show()
                        ISearchEvent.UnknownError -> Toast.makeText(
                            ctx,
                            CoreUiRes.string.unknown_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            StatelessSearchAppBar(
                title = ctx.getString(R.string.header_new_rss),
                onBackPressed = viewModel::goBack
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            StatelessSearch(
                searchState = searchState.value,
                placeholder = ctx.getString(CoreUiRes.string.search_placeholder),
                noteText = "",
                onAddClick = viewModel::addRss,
                onTextChange = viewModel::updateValue
            )
        }
    }
}

@Composable
@Preview(
    apiLevel = 33,
    heightDp = 400,
    widthDp = 300,
    showBackground = true
)
@DebugOnly
fun PreviewSearchScreen() {
    MaterialTheme {
        SearchScreen()
    }
}

