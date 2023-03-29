package com.mukiva.rssreader.addrss.parsing.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.util.*

@Entity
data class ItemEntity(
    @Id
    var id: Long = 0,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    val pubDate: Date? = null,
    val enclosureUrl: String? = null,
    val enclosureLength: Long? = null,
    val enclosureType: String? = null
) {
    lateinit var channel: ToOne<ChannelEntity>
}