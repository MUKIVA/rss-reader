package com.mukiva.feature.feed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.search.common.domian.IRssMeta
import com.mukiva.feature.feed.presentation.FeedPage
import com.mukiva.feature.feed.presentation.FeedPagnator
import com.mukiva.navigation.ui.theme.RSSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RSSReaderAppBar(
    title: String,
    showMenu: Boolean,
    aboutFeedText: String,
    onAboutFeed: () -> Unit,
    addFeedText: String,
    onAddFeed: () -> Unit,
    deleteFeedText: String,
    onDeleteFeed: () -> Unit,
    onMenuClick: () -> Unit,
    pages: Collection<FeedPage>,
    selectedTabIndex: Int,
    onTabClick: (Int) -> Unit
) {
    Column {
        TopAppBar(
            title = { Text(title, style = MaterialTheme.typography.headlineMedium) },
            actions = {
                if (pages.isNotEmpty()) {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.MoreVert, "")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = onMenuClick
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = aboutFeedText) },
                            onClick = onAboutFeed
                        )
                        DropdownMenuItem(
                            text = { Text(text = addFeedText) },
                            onClick = onAddFeed
                        )
                        DropdownMenuItem(
                            text = { Text(text = deleteFeedText) },
                            onClick = onDeleteFeed
                        )
                    }
                }
            }
        )
        if (pages.isNotEmpty())
            TabRow(selectedTabIndex = selectedTabIndex) {
                pages.forEachIndexed { index, feedPage ->
                    Tab(
                        selected = feedPage.isSelected,
                        onClick = { onTabClick(index) }
                    ) {
                        Text(
                            text = feedPage.name,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
    }
}

@Composable
@Preview
@DebugOnly
fun PreviewRSSReaderAppBar() {
    RSSTheme {
        RSSReaderAppBar(
            title = "RSSReader",
            showMenu = false,
            onAboutFeed = {},
            aboutFeedText = "About Feed",
            onAddFeed = {},
            addFeedText = "Add Feed",
            onDeleteFeed = {},
            deleteFeedText = "Delete Feed",
            onMenuClick = {},
            selectedTabIndex = 1,
            onTabClick = {},
            pages = buildList {
                add(FeedPage(
                    isSelected = true,
                    articleStateCollection = emptyList(),
                    type = FeedPage.Type.LOADING,
                    meta = object : IRssMeta {
                        override val name: String
                            get() = "Sample One"
                        override val url: String
                            get() = ""
                        override val originalUrl: String
                            get() = ""
                        override val description: String
                            get() = ""
                        override val imageUrl: String
                            get() = ""

                    },
                    bottomProgressType = FeedPagnator.ProgressType.CONTENT
                ))
                add(FeedPage(
                    isSelected = true,
                    articleStateCollection = emptyList(),
                    type = FeedPage.Type.CONTENT,
                    meta = object : IRssMeta {
                        override val name: String
                            get() = "Sample Looooooooooooong"
                        override val url: String
                            get() = ""
                        override val originalUrl: String
                            get() = ""
                        override val description: String
                            get() = ""
                        override val imageUrl: String
                            get() = ""

                    },
                    bottomProgressType = FeedPagnator.ProgressType.CONTENT
                ))
            }
        )
    }
}