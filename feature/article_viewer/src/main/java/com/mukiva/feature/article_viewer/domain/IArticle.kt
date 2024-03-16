package com.mukiva.feature.article_viewer.domain

import java.util.Date

interface IArticle {
    val title: String
    val pubDate: Date
    val imgUrl: String
    val content: String
    val newsUrl: String
}