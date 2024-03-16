package com.mukiva.feature.search.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mukiva.core.utils.domain.DebugOnly
import com.mukiva.feature.search.common.R
import com.mukiva.feature.search.common.presentation.SearchState

@Composable
fun StatelessSearch(
    searchState: SearchState,
    placeholder: String,
    noteText: String,
    onAddClick: () -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        if (noteText.isNotBlank())
            Text(
                text = noteText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        Spacer(modifier = Modifier.padding(14.dp))
        Box(
            contentAlignment = Alignment.CenterEnd
        ) {
            OutlinedTextField(
                value = searchState.value,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = placeholder) }
            )
            if (searchState.resultState == SearchState.IResultState.Loading)
                Box(modifier = Modifier.padding(end = 15.dp)) {
                    CircularProgressIndicator(modifier = Modifier)
                }
        }
        Spacer(Modifier.padding(8.dp))
        when (searchState.resultState) {
            is SearchState.IResultState.ErrorState -> {
                Text(
                    text = when (searchState.resultState.errorType) {
                        SearchState.IResultState.ErrorState.ErrorType.UNKNOWN ->
                            ctx.getString(R.string.unknown_error)
                        SearchState.IResultState.ErrorState.ErrorType.INVALID_URL ->
                            ctx.getString(R.string.invalid_url_format)
                        SearchState.IResultState.ErrorState.ErrorType.CONNECTION ->
                            ctx.getString(R.string.connection_fail)
                        SearchState.IResultState.ErrorState.ErrorType.URL_ALREADY_EXISTS ->
                            ctx.getString(R.string.url_aready_exists)
                        SearchState.IResultState.ErrorState.ErrorType.TIME_OUT ->
                            ctx.getString(R.string.time_out)

                    },
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is SearchState.IResultState.SuccessState -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = MaterialTheme.shapes.small
                    ) {
                        AsyncImage(
                            model = searchState.resultState.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .background(MaterialTheme.colorScheme.surfaceDim)
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Column {
                        Text(
                            text = searchState.resultState.title,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = searchState.resultState.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = onAddClick,
                    shape = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = ctx.getString(R.string.add))
                }
            }
            else -> {}
        }
    }
}

@Composable
@Preview(
    apiLevel = 33,
    showBackground = true
)
@DebugOnly
fun PreviewSearch() {
    MaterialTheme {
        StatelessSearch(searchState = SearchState(
            value = "",
            resultState = SearchState.IResultState.SuccessState(
                title = "Lenta.ru",
                description = "asasdasdasdasdasdasdasdasdasdasdasd",
                imageUrl = "",
            )
        ),
            onAddClick = {},
            placeholder = "Enter URL",
            noteText = "DDASDASDASd",
            onTextChange = {}
        )
    }
}