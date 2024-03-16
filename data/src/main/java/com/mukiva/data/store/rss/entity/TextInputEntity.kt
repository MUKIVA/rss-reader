package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class TextInputEntity(
    @Id
    var id: Long = 0,
    val title: String,
    val description: String,
    val name: String,
    val link: String
)