package com.mukiva.feature.article_viewer.presentation

sealed interface IArticleViewerEvent {
    data class Share(val url: String) : IArticleViewerEvent
    data class OpenOriginal(val url: String) : IArticleViewerEvent
}