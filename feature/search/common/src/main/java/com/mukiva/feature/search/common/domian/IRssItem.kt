package com.mukiva.feature.search.common.domian

import java.util.Date

interface IRssItem {
    val id: Long
    val name: String
    val content: String
    val pubDate: Date
    val imgUrl: String
    val imageType: String
}