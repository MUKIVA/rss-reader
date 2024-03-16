package com.mukiva.data.mapper.builders

import com.mukiva.data.entity.TextInput

class TextInputBuilder {
    var title: String = ""
    var description: String = ""
    var name: String = ""
    var link: String = ""

    fun build(): TextInput {
        return TextInput(
            title =  title,
            description =  description,
            name =  name,
            link =  link
        )
    }
}