package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CloudEntity(
    @Id
    var id: Long = 0,
    val domain: String,
    val port: String,
    val path: String,
    val registerProcedure: String,
    val protocol: String
)