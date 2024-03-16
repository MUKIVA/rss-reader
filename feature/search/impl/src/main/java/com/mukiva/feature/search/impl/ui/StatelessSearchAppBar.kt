@file:OptIn(ExperimentalMaterial3Api::class)

package com.mukiva.feature.search.impl.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mukiva.core.utils.domain.DebugOnly

@Composable
fun StatelessSearchAppBar(
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(
                onClick = onBackPressed
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
            }
        }
    )
}

@Composable
@Preview(
    apiLevel = 33
)
@DebugOnly
fun PreviewSearchAppBar() {
    MaterialTheme {
        StatelessSearchAppBar(
            title = "Rss Reader",
            onBackPressed = {}
        )
    }
}