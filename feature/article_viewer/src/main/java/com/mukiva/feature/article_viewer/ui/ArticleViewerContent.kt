package com.mukiva.feature.article_viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mukiva.core.ui.DateFormatter
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.article_viewer.R
import com.mukiva.feature.article_viewer.domain.IArticle
import com.mukiva.feature.article_viewer.presentation.ArticleViewerState

@Composable
fun StatelessArticleViewerContent(
    state: IArticle,
    horizontalContentPadding: Dp,
    onOpenOriginal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = state.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal =  horizontalContentPadding)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = DateFormatter.formatAsDayOfYear(state.pubDate),
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal =  horizontalContentPadding)
        )
        Spacer(modifier = Modifier.size(14.dp))
        if (state.imgUrl.isNotBlank())
            AsyncImage(
                model = state.imgUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .height(200.dp)
            )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = state.content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 16.sp,
            modifier = Modifier
                .padding(horizontal = horizontalContentPadding)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextButton(
            onClick = onOpenOriginal,
            modifier = Modifier.padding(horizontal = horizontalContentPadding)
        ) {
            Text(
                text = ctx.getString(R.string.open_original_news),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview(
    showBackground = true,
    heightDp = 1000
)
@Composable
@DebugOnly
fun PreviewArticleViewerContent() {
    MaterialTheme {
        StatelessArticleViewerContent(
            state = ArticleViewerState.mock().news,
            horizontalContentPadding = 16.dp,
            onOpenOriginal = {}
        )
    }
}