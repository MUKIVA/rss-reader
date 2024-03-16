package com.mukiva.feature.feed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukiva.core.utils.domain.DebugOnly

@Composable
fun StatelessAboutRssItem(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W900
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.size(20.dp))
        TextButton(
            onClick = onButtonClick
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
@Preview(
    apiLevel = 33,
    showBackground = true,
    showSystemUi = true
)
@DebugOnly
fun PreviewAboutRssItem() {
    MaterialTheme {
        StatelessAboutRssItem(
            title = "Lenta.ru",
            description = "ASDASDASDASD",
            buttonText = "Some action",
            onButtonClick = {}
        )
    }
}


