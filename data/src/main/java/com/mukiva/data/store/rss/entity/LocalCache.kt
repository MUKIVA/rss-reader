package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToOne
import java.util.Date

@Entity
data class LocalCache(
    @Id
    var id: Long = 0,
    @Unique
    val url: String? = null,
    val lastUpdate: Date = Date()
) {
    lateinit var rssId: ToOne<RssEntity>
}

