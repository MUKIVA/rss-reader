package com.mukiva.data.entity

data class Image(
    val id: Long = 0,
    val url: String = "",
    val title: String = "",
    val link: String = "",
    val width: Int? = null,
    val height: Int? = null,
    val description: String? = null
)