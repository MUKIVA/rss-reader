package com.mukiva.feature.feed.domain

import java.util.Date

interface IArticle {
    val id: Long
    val title: String
    val body: String
    val date: Date
    val imgUrl: String
}
