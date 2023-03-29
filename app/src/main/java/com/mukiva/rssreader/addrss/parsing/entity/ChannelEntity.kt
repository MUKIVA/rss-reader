package com.mukiva.rssreader.addrss.parsing.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany

@Entity
data class ChannelEntity(
    @Id
    var id: Long = 0,
    @Unique
    val title: String = "",
    val link: String = "",
    val description: String = "",
    val imageUrl: String? = null

) {
    @Backlink(to = "channel")
    lateinit var items: ToMany<ItemEntity>
}