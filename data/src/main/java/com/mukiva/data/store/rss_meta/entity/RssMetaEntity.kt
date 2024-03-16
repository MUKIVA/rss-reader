package com.mukiva.data.store.rss_meta.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique


@Entity
data class RssMetaEntity(
    @Id
    var id: Long = 0,
    @Unique
    val url: String = "",
    val originalUrl: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",

)