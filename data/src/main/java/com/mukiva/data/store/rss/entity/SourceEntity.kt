package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class SourceEntity(
    @Id
    var id: Long = 0,
    val url: String,
    val text: String
)