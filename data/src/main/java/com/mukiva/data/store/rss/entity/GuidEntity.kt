package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class GuidEntity(
    @Id
    var id: Long = 0,
    val text: String,
    val isPermaLink: Boolean?
)