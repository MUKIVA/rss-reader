package com.mukiva.data.store.rss.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.util.Date

@Entity
data class ChannelEntity(
    @Id
    var id: Long = 0,
    val refreshLink: String = "",
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val language: String? = null,
    val copyright: String? = null,
    val managingEditor: String? = null,
    val webMaster: String? = null,
    val pubDate: Date? = null,
    val lastBuildDate: Date? = null,
    val generator: String? = null,
    val docs: String? = null,
    val ttl: String? = null,
    val rating: String? = null,
    val skipHours: Int? = null,
    val skipDays: Int? = null,
) {
    @Backlink(to = "channel")
    lateinit var items: ToMany<ItemEntity>
    lateinit var category: ToMany<CategoryEntity>
    lateinit var cloud: ToOne<CloudEntity>
    lateinit var image: ToOne<ImageEntity>
    lateinit var textInput: ToOne<TextInputEntity>
}