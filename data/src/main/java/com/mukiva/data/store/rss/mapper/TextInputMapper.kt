package com.mukiva.data.store.rss.mapper

import com.mukiva.data.entity.TextInput
import com.mukiva.data.store.rss.entity.TextInputEntity
import javax.inject.Inject

class TextInputMapper @Inject constructor() {

    fun mapAsEntity(textInput: TextInput) = TextInputEntity(
        id = textInput.id,
        title = textInput.title,
        description = textInput.description,
        name = textInput.name,
        link = textInput.link,
    )

    fun mapAsDomain(entity: TextInputEntity) = TextInput(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        name = entity.name,
        link = entity.link,
    )

}