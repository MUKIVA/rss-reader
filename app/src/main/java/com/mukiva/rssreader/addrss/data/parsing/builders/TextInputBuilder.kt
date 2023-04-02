package com.mukiva.rssreader.addrss.data.parsing.builders

import com.mukiva.rssreader.addrss.data.parsing.elements.TextInput

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