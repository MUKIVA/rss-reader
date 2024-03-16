package com.mukiva.feature.feed.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.navigation.ui.theme.RSSTheme

@Composable
fun StatelessNewsItem(
    label: String,
    body: String,
    date: String,
    imgUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Surface(
                shape = MaterialTheme.shapes.small
            ) {
                if (imgUrl.isNotBlank())
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .size(72.dp)
                    )
                else
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "",
                        modifier = Modifier
                            .size(72.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(16.dp)

                    )
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W900,
                    lineHeight = TextUnit(20.0f, TextUnitType.Sp)
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}


@Composable
@Preview(
    apiLevel = 33,
    showBackground = true,
)
@Preview(
    apiLevel = 33,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@DebugOnly
fun PreviewNewsItem() {
    RSSTheme {
        Surface {
            StatelessNewsItem(
                "label\nLABEL",
                "body",
                "date",
                "",
                onClick = {}
            )
        }
    }
}
