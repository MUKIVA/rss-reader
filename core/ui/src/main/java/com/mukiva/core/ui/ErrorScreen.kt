package com.mukiva.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    image: ImageVector? = null,
    imageColor: ColorFilter? = null
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            image?.let {
                Image(
                    imageVector = it,
                    colorFilter = imageColor,
                    contentDescription = null,
                    modifier = Modifier.size(166.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = { onButtonClick() }) {
                Text(text = buttonText)
            }
        }
    }
}

@Composable
@Preview
fun PreviewErrorScreen() {
    MaterialTheme {
        ErrorScreen(
            text = "В ленте еще нет новостей",
            buttonText = "Обновить",
            onButtonClick = {},
            image = Icons.Rounded.Warning,
            imageColor = ColorFilter.tint(MaterialTheme.colorScheme.error)
        )
    }
}