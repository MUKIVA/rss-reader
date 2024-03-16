package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.util.Date

@Entity
data class ItemEntity(
    @Id
    var id: Long = 0,
    val title: String? = null,
    val description: String? = null,
    val link: String? = null,
    val pubDate: Date? = null,
    val author: String? = null,
    val comments: String? = null,
) {
    lateinit var guid: ToOne<GuidEntity>
    lateinit var enclosure: ToOne<EnclosureEntity>
    lateinit var category: ToMany<CategoryEntity>
    lateinit var source: ToOne<SourceEntity>

    lateinit var channel: ToOne<ChannelEntity>
}