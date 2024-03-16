package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class RssEntity(
    @Id
    var id: Long = 0,
    val version: String = "1.0",
) {
    lateinit var channel: ToOne<ChannelEntity>
}