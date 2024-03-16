package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ImageEntity(
    @Id
    var id: Long = 0,
    val url: String = "",
    val title: String = "",
    val link: String = "",
    val width: Int? = null,
    val height: Int? = null,
    val description: String? = null
)